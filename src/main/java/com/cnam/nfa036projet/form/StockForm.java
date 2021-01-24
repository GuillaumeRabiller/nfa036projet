package com.cnam.nfa036projet.form;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockForm {

    private long id ;

    private String nomProduit ;

    private String categorie ;

    private LocalDateTime dateEntree ;

    private LocalDateTime dlc ;

    private String statut ;

    private int colorStatut ;

    public StockForm(long id, String nomProduit, String categorie, LocalDateTime dateEntree, LocalDateTime dlc, String statut, int colorStatut) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.categorie = categorie;
        this.dateEntree = dateEntree;
        this.dlc = dlc;
        this.statut = statut;
        this.colorStatut = colorStatut ;
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

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDateTime dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDateTime getDlc() {
        return dlc;
    }

    public void setDlc(LocalDateTime dlc) {
        this.dlc = dlc;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getColorStatut() {
        return colorStatut;
    }

    public void setColorStatut(int colorStatut) {
        this.colorStatut = colorStatut;
    }
}
