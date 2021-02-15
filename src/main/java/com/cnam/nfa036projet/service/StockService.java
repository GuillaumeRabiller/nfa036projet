package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.CreateStockForm;
import com.cnam.nfa036projet.form.StockForm;
import com.cnam.nfa036projet.model.Produit;
import com.cnam.nfa036projet.model.Statut;
import com.cnam.nfa036projet.model.Stock;
import com.cnam.nfa036projet.model.StockHistorique;
import com.cnam.nfa036projet.repository.ProduitRepository;
import com.cnam.nfa036projet.repository.StatutRepository;
import com.cnam.nfa036projet.repository.StockHistoriqueRepository;
import com.cnam.nfa036projet.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class StockService {

    @Autowired
    private StockRepository stockRepository ;

    @Autowired
    private StatutRepository statutRepository ;

    @Autowired
    private StockHistoriqueRepository stockHistoriqueRepository ;

    @Autowired
    private ProduitRepository produitRepository ;

    @Autowired
    private UtilisateurService userService ;


    // Méthode d'envoi de la Liste de Produits en Stock, étant appelée après chaque opération

    public List<StockForm> listeStock() {
        List<Stock> stocks = stockRepository.findAll();
        List<StockForm> stockList = new ArrayList<>();
        for (Stock stock:stocks) {

            //MISE A JOUR DU STATUT SI DLC COURTE = INFERIEUR A 24 HEURES + SAUVEGARDE NV STATUT EN BASE + SAUVEGARDE HISTORIQUE
            LocalDateTime dateEntree = stock.getDateEntree().truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
            dlc.truncatedTo(ChronoUnit.SECONDS);
            long daysBetween = LocalDateTime.now().until(dlc, ChronoUnit.HOURS);
            if (stock.getStatut().getNomStatut().equals("En Stock") && daysBetween <= 24) {
                Statut statut = statutRepository.findByNomStatut("A Contrôler");
                stock.changeStatut(statut);
                stockRepository.save(stock);

                // AJOUT D'UN NOUVEL HISTORIQUE
                StockHistorique historique = new StockHistorique();
                historique.setDateMouvementStock(LocalDateTime.now());
                historique.setProduit(stock.getProduit().getNomProduit());
                historique.setIdProduit(stock.getId());
                historique.setUtilisateur(userService.getNomUser());
                historique.setStatut(statut.getNomStatut());
                historique.setCategorie(stock.getProduit().getCategorie().getNomCategorie());
                stockHistoriqueRepository.save(historique);
            }

            //On renseigne stockForm
            long id = stock.getId();
            String nomProduit = stock.getProduit().getNomProduit();
            String categorie = stock.getProduit().getCategorie().getNomCategorie();
            String statut = stock.getStatut().getNomStatut();
            int nbStatut = 0 ;
            if (daysBetween > 24) {
                nbStatut = 1;
            } else if (daysBetween >= 0) {
                nbStatut = 2;
            } else nbStatut = 3;

            StockForm aStock = new StockForm(id, nomProduit, categorie, dateEntree, dlc, statut, nbStatut);
            stockList.add(aStock);
        }
        return stockList ;
    }

    public Stock enterStock(Produit produit){
        Stock stock = new Stock();
        stock.setDateEntree(LocalDateTime.now());
        produit.addStock(stock);
        Statut statut = statutRepository.findByNomStatut("En Stock");
        statut.addStock(stock);
        return stock ;
    }

}
