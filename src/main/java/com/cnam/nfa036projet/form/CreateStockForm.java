package com.cnam.nfa036projet.form;

import com.cnam.nfa036projet.model.Produit;

import java.util.List;

public class CreateStockForm {

    private List<Produit> listProduit ;

    private long produitId ;

    public CreateStockForm() {
        super();
    }

    public List<Produit> getListProduit() {
        return listProduit;
    }

    public void setListProduit(List<Produit> listProduit) {
        this.listProduit = listProduit;
    }

    public long getProduitId() {
        return produitId;
    }

    public void setProduitId(long produitId) {
        this.produitId = produitId;
    }
}
