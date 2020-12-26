package com.cnam.nfa036projet.form;

import com.cnam.nfa036projet.model.Statut;

import java.time.LocalDate;

public class StockForm {

    private long id ;

    private String nomProduit ;

    private String categorie ;

    private LocalDate dateEntree ;

    private LocalDate dlc ;

    private LocalDate dateSortie ;

    private String statut ;

    public StockForm(long id, String nomProduit, String categorie, LocalDate dateEntree, LocalDate dlc, LocalDate dateSortie, String statut) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.categorie = categorie;
        this.dateEntree = dateEntree;
        this.dlc = dlc;
        this.dateSortie = dateSortie;
        this.statut = statut;
    }

    public StockForm(){
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDate getDlc() {
        return dlc;
    }

    public void setDlc(LocalDate dlc) {
        this.dlc = dlc;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
