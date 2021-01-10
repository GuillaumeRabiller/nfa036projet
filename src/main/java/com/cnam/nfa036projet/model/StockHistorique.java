package com.cnam.nfa036projet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StockHistorique {

    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_STOCK")
    private long id ;

    @Column(name = "DATE_MOUVEMENT_STOCK", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateMouvementStock ;

    @Column (name="PRODUIT_STOCK", nullable = false)
    private String produit ;

    @Column (name = "STATUT", nullable = false)
    private String statut ;

    @Column (name = "ID_PRODUIT", nullable = false)
    private long idProduit ;

    @Column (name = "UTILISATEUR", nullable = false)
    private String utilisateur ;

    @Column (name = "CATEGORIE", nullable = false)
    private String categorie ;



    //CONSTUCTEURS

    public StockHistorique(){
        super();
    }




    //GETTERS ET SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateMouvementStock() {
        return dateMouvementStock;
    }

    public void setDateMouvementStock(LocalDateTime dateMouvementStock) {
        this.dateMouvementStock = dateMouvementStock;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(long idProduit) {
        this.idProduit = idProduit;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
