package com.cnam.nfa036projet.form;

import java.time.LocalDateTime;

public class EtiquetteForm {

    private long id ;

    private String nomProduit ;

    private LocalDateTime dateEntree ;

    private LocalDateTime dlc ;

    public EtiquetteForm(long id, String nomProduit, LocalDateTime dateEntree, LocalDateTime dlc) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.dateEntree = dateEntree;
        this.dlc = dlc;
    }

    public EtiquetteForm(){
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

}
