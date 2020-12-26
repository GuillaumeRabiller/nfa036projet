package com.cnam.nfa036projet.controller;


import com.cnam.nfa036projet.form.*;
import com.cnam.nfa036projet.model.*;
import com.cnam.nfa036projet.repository.ProduitRepository;
import com.cnam.nfa036projet.repository.StatutRepository;
import com.cnam.nfa036projet.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StockController {

    //Création d'un StockRepository

    @Autowired
    private StockRepository stockRepository ;

    @Autowired
    private ProduitRepository produitRepository ;

    @Autowired
    private StatutRepository statutRepository ;

    public Statut findByNomStatut(String nomStatut){
        Statut statut = statutRepository.findByNomStatut(nomStatut);
        return statut ;
    }

    //Méthode d'envoi de la Liste de Produits en Stock, étant appelée après chaque opération

    public List<StockForm> listeStock() {
        List<Stock> stocks = stockRepository.findAll();
        List<StockForm> stockList = new ArrayList<>();
        for (Stock stock:stocks) {
            //on n'affiche que les produits en cours
            if( stock.getStatut().getNomStatut().equals("En Stock") ||
                stock.getStatut().getNomStatut().equals("A Contrôler")) {

                //MISE A JOUR DU STATUT SI DLC COURTE = INFERIEUR A 24 HEURES + SAUVEGARDE NV STATUT EN BASE
                LocalDate dateEntree = stock.getDateEntree();
                LocalDate dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
                long daysBetween = LocalDate.now().until(dlc, ChronoUnit.DAYS);
                if (stock.getStatut().getNomStatut().equals("En Stock") && daysBetween <= 1) {
                    Statut statut = findByNomStatut("A Contrôler");
                    stock.changeStatut(statut);
                    stockRepository.save(stock);
                }

                //On renseigne stockForm
                long id = stock.getId();
                String nomProduit = stock.getProduit().getNomProduit();
                String categorie = stock.getProduit().getCategorie().getNomCategorie();
                String statut = stock.getStatut().getNomStatut();
                LocalDate dateSortie = stock.getDateSortie();

                StockForm aStock = new StockForm(id, nomProduit, categorie, dateEntree, dlc, dateSortie, statut);
                stockList.add(aStock);
            }
        }
        return stockList ;
    }

    //HISTORIQUE DES STOCKS SELON UNE DATE

    public List<StockForm> historiqueStock(LocalDate date) {
        List<Stock> stocks = stockRepository.findAll();
        List<StockForm> stockList = new ArrayList<>();
        for (Stock stock:stocks) {
            //renseignement de la liste selon la date
            if(     (stock.getDateEntree().isEqual(date) || stock.getDateEntree().isBefore(date)) &&
                    (stock.getDateSortie() == null || stock.getDateSortie().isEqual(date) || stock.getDateSortie().isAfter(date)) )
            {

                LocalDate dateEntree = stock.getDateEntree();
                LocalDate dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
                long id = stock.getId();
                String nomProduit = stock.getProduit().getNomProduit();
                String categorie = stock.getProduit().getCategorie().getNomCategorie();
                String statut = stock.getStatut().getNomStatut();
                LocalDate dateSortie = stock.getDateSortie();

                StockForm aStock = new StockForm(id, nomProduit, categorie, dateEntree, dlc, dateSortie, statut);
                stockList.add(aStock);
            }
        }
        return stockList ;
    }


    /**
     * LISTE DES PRODUITS EN MOUVEMENT DE STOCK
     *
     * READ
     *
     * EN COURS
     *
     */

    @GetMapping("/readStock")
    public String readStock(Model model) {
        model.addAttribute("stockList", listeStock());
        DateStockForm dateStock = new DateStockForm();
        model.addAttribute("dateStock",dateStock);
        return "/Stock/readStock";
    }

    @PostMapping("/readHistoriqueStock")
    public String readHistorique(@ModelAttribute("dateStock") DateStockForm dateHistorique, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return readStock(model);
        } else {
            model.addAttribute("historiqueList", historiqueStock(LocalDate.parse(dateHistorique.getDate())));
            DateStockForm dateStock = new DateStockForm();
            model.addAttribute("dateStock",dateStock);
            return "/Stock/readHistoriqueStock";
        }
    }


    /**
     * FORMULAIRE DE SAISIE ENTREE PRODUIT EN STOCK
     *
     * CREATE
     *
     * A TEST
     */

    @RequestMapping(value = {"/createStock"}, method = RequestMethod.GET)
    public String createStock(Model model) {
        CreateStockForm aStock = new CreateStockForm();
        aStock.setListProduit(produitRepository.findAll());
        model.addAttribute("aStock", aStock);
        return "/Stock/createStock";
    }


    /**
     * SAUVEGARDE FORMULAIRE ENTREE EN STOCK
     *
     * SAVE
     *
     * A TEST
     */

    @RequestMapping(value = {"/saveStock"}, method = RequestMethod.POST)
    public String saveStock(@ModelAttribute("aStock") CreateStockForm aStock, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createStock(model);
        } else {
            Stock stock = new Stock();
            stock.setDateEntree(LocalDate.now());
            Optional<Produit> produit = produitRepository.findById(aStock.getProduitId());
            produit.ifPresent(product -> {
                product.addStock(stock);
            });
            Statut statut = findByNomStatut("En Stock");
            statut.addStock(stock);

            stockRepository.save(stock);
        }
        //Retour à la liste en Stock
        return readStock(model);
    }

    /**
     * PAGE DE UPDATE STOCK VIA ID
     *
     * UPDATE
     *
     * A TESTER
     */

    @GetMapping("/updateStock/{id}")
    public String updateStockFormulaire(@PathVariable("id") long id, Model model) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid stock id:" + id));
        UpdateStockForm aStock = new UpdateStockForm(stock.getId(),stock.getProduit().getNomProduit());
        model.addAttribute("aStock", aStock);
        return "/Stock/updateStock";
    }

    @PostMapping("/updateStock")
    public String updateStock(@Valid UpdateStockForm aStock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //retour à la liste En Stock
            return readStock(model);
        }
        Stock stock = stockRepository.findById(aStock.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid stock id"));
        //Recupération du Statut entré dans le formulaire
        Statut statut = findByNomStatut(aStock.getStatut());
        //On change le statut du stock
        stock.changeStatut(statut);
        //Si le statut retire le produit du stock, on note une date de retrait
        if( statut.getNomStatut().equals("Retiré") ||
            statut.getNomStatut().equals("Erreur") ||
            statut.getNomStatut().equals("Consommé")){
            stock.setDateSortie(LocalDate.now());
        }

        stockRepository.save(stock);

        return readStock(model);
    }

}
