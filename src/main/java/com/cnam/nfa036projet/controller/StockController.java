package com.cnam.nfa036projet.controller;


import com.cnam.nfa036projet.form.*;
import com.cnam.nfa036projet.model.*;
import com.cnam.nfa036projet.repository.*;
import com.cnam.nfa036projet.service.*;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Controller
public class StockController {

    //Création d'un StockRepository

    @Autowired
    private StockRepository stockRepository ;

    @Autowired
    private ProduitRepository produitRepository ;

    @Autowired
    private StatutRepository statutRepository ;

    @Autowired
    private StockHistoriqueRepository stockHistoriqueRepository ;

    @Autowired
    private StockService stockService ;

    @Autowired
    private StockHistoriqueService stockHistoriqueService ;

    @Autowired
    private UtilisateurService userService ;

/*
    public Statut findByNomStatut(String nomStatut){
        Statut statut = statutRepository.findByNomStatut(nomStatut);
        return statut ;
    }
*/



    /**
     * LISTE DES PRODUITS EN MOUVEMENT DE STOCK
     *
     * READ
     *
     *
     */

    @RequestMapping(value = { "/readStock"}, method = RequestMethod.GET)
    public String readStock(Model model) {
        model.addAttribute("stockList", stockService.listeStock());
        DateStockForm dateStock = new DateStockForm();
        model.addAttribute("dateStock",dateStock);
        return "/Stock/readStock";
    }

    /**
     * HISTORIQUE DES PRODUITS A UNE DATE DONNEE
     *
     * READ
     *
     *
     */

    @PostMapping("/readHistoriqueStock")
    public String readHistorique(@ModelAttribute("dateStock") DateStockForm dateHistorique, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return readStock(model);
        } else {
            model.addAttribute("historiqueList", stockHistoriqueService.historiqueStock(LocalDate.parse(dateHistorique.getDate())));
            DateStockForm dateStock = new DateStockForm();
            model.addAttribute("dateStock",dateStock);
            return "/Stock/readHistoriqueStock";
        }
    }


    /**
     * HISTORIQUE DES PRODUITS SELON ID
     *
     * READ
     *
     *
     */

    @PostMapping("/readHistoriqueStockById")
    public String readHistoriqueById(@ModelAttribute("dateStock") DateStockForm dateHistorique, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return readStock(model);
        } else {
            model.addAttribute("historiqueList", stockHistoriqueService.historiqueStockById(dateHistorique.getId()));
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
     * SAVE + HISTORISATION DU MOUVEMENT
     *
     */

    @RequestMapping(value = {"/saveStock"}, method = RequestMethod.POST)
    public String saveStock(@ModelAttribute("aStock") CreateStockForm aStock, BindingResult bindingResult, Model model) throws IOException, DocumentException {
        if (bindingResult.hasErrors()) {
            createStock(model);
        } else {
            Stock stock = new Stock();
            StockHistorique historique = new StockHistorique();
            stock.setDateEntree(LocalDateTime.now());
            historique.setDateMouvementStock(LocalDateTime.now());
            Optional<Produit> produit = produitRepository.findById(aStock.getProduitId());
            produit.ifPresent(product -> {
                product.addStock(stock);
                historique.setProduit(product.getNomProduit());
                historique.setCategorie(product.getCategorie().getNomCategorie());
            });
            Statut statut = statutRepository.findByNomStatut("En Stock");
            statut.addStock(stock);
            historique.setStatut(statut.getNomStatut());
            historique.setUtilisateur(userService.getNomUser());

            stockRepository.save(stock);
            historique.setIdProduit(stock.getId());
            stockHistoriqueRepository.save(historique);

            //Génération de l'étiquette PDF
            LocalDateTime dateEntree = stock.getDateEntree().truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
            dlc.truncatedTo(ChronoUnit.SECONDS);
            EtiquetteForm etiquette = new EtiquetteForm(stock.getId(), stock.getProduit().getNomProduit(), dateEntree, dlc );
            String templateHtml = EtiquetteService.parseEtiquetteTemplate(etiquette) ;
            EtiquetteService.generatePdfFromHtml (templateHtml) ;
        }
        //Retour à la liste en Stock
        return readStock(model);
    }

    /**
     * PAGE DE UPDATE STOCK VIA ID
     *
     * UPDATE
     *
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
        Statut statut = statutRepository.findByNomStatut(aStock.getStatut());

        //Si le statut retire le produit du stock, on le supprime du Stock
        if( statut.getNomStatut().equals("Retiré") ||
            statut.getNomStatut().equals("Erreur") ||
            statut.getNomStatut().equals("Consommé")){
            stock.getProduit().deleteStock(stock);
            stock.getStatut().deleteStock(stock);
            stockRepository.delete(stock);
        } else {   //On change le statut du stock et on sauvegarde
            stock.changeStatut(statut);
            stockRepository.save(stock);
        }

        // AJOUT D'UN NOUVEL HISTORIQUE
        StockHistorique historique = new StockHistorique();
        historique.setDateMouvementStock(LocalDateTime.now());
        historique.setProduit(stock.getProduit().getNomProduit());
        historique.setIdProduit(stock.getId());
        historique.setUtilisateur(userService.getNomUser());
        historique.setStatut(statut.getNomStatut());
        historique.setCategorie(stock.getProduit().getCategorie().getNomCategorie());
        stockHistoriqueRepository.save(historique);

        return readStock(model);
    }


}
