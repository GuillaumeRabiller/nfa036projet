package com.cnam.nfa036projet.form;

import java.time.LocalDateTime;

public class HistoriqueForm {

    private long idProduit ;

    private String nomProduit ;

    private String categorie ;

    private LocalDateTime dateMouvement ;

    private String statut ;

    private String utilisateur ;

    public HistoriqueForm(long idProduit, String nomProduit, String categorie, String statut, LocalDateTime dateMouvement, String utilisateur) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.categorie = categorie;
        this.statut = statut;
        this.dateMouvement = dateMouvement ;
        this.utilisateur = utilisateur ;
    }

    public HistoriqueForm(){
        super();
    }

    public long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(long idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
