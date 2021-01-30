package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.HistoriqueForm;
import com.cnam.nfa036projet.model.StockHistorique;
import com.cnam.nfa036projet.repository.StockHistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StockHistoriqueService {

    @Autowired
    private StockHistoriqueRepository stockHistoriqueRepository ;

    //HISTORIQUE DES STOCKS SELON UNE DATE

    public List<HistoriqueForm> historiqueStock(LocalDate date) {
        List<StockHistorique> stocks = stockHistoriqueRepository.findAll();
        List<HistoriqueForm> stockList = new ArrayList<>();
        for (StockHistorique stock:stocks) {
            //renseignement de la liste selon la date
            if( stock.getDateMouvementStock().toLocalDate().isEqual(date) )
            {
                long idProduit = stock.getIdProduit();
                String nomProduit = stock.getProduit();
                String categorie = stock.getCategorie();
                String statut = stock.getStatut();
                String utilisateur = stock.getUtilisateur();
                LocalDateTime dateMouvement = stock.getDateMouvementStock().truncatedTo(ChronoUnit.SECONDS);

                HistoriqueForm aStock = new HistoriqueForm(idProduit, nomProduit, categorie, statut, dateMouvement, utilisateur);
                stockList.add(aStock);
            }
        }
        //Tri de la liste selon ID stock
        //A FAIRE
        //

        return stockList ;
    }


    //HISTORIQUE DES STOCKS SELON UNE ID PRODUIT

    public List<HistoriqueForm> historiqueStockById(long id) {
        List<StockHistorique> stocks = stockHistoriqueRepository.findAll();
        List<HistoriqueForm> stockList = new ArrayList<>();
        for (StockHistorique stock:stocks) {
            //renseignement de la liste selon l'ID produit
            if( stock.getIdProduit() == id )
            {
                long idProduit = stock.getIdProduit();
                String nomProduit = stock.getProduit();
                String categorie = stock.getCategorie();
                String statut = stock.getStatut();
                String utilisateur = stock.getUtilisateur();
                LocalDateTime dateMouvement = stock.getDateMouvementStock().truncatedTo(ChronoUnit.SECONDS);

                HistoriqueForm aStock = new HistoriqueForm(idProduit, nomProduit, categorie, statut, dateMouvement, utilisateur);
                stockList.add(aStock);
            }
        }
        //Tri de la liste selon DATE
        //A FAIRE
        //

        return stockList ;
    }


}
