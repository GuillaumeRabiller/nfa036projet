package com.cnam.nfa036projet.form;

public class UpdateStockForm {

    private long id ;
    private String nomProduit ;
    private String statut ;

    public UpdateStockForm(long id, String nomProduit) {
        this.id = id;
        this.nomProduit = nomProduit;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
