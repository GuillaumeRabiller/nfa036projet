package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.StockForm;
import com.cnam.nfa036projet.model.Statut;
import com.cnam.nfa036projet.model.Stock;
import com.cnam.nfa036projet.model.StockHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class StockService {

    private static JpaRepository<Stock, Long> stockRepository;
    private static JpaRepository<Statut, Long> statutRepository;
    private static JpaRepository<StockHistorique, Long> stockHistoriqueRepository;



    //Méthode d'envoi de la Liste de Produits en Stock, étant appelée après chaque opération

    public static List<StockForm> listeStock() {
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
                historique.setUtilisateur(UtilisateurDetailsService.getNomUser());
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



}
