package com.cnam.nfa036projet.form;

import com.cnam.nfa036projet.model.Categorie;
import com.cnam.nfa036projet.model.Produit;

import java.util.List;

public class CreateStockForm {

    private List<Produit> listProduit ;

    private long produitId ;

    private List<Categorie> listCategorie ;

    private long categorieId ;

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

    public List<Categorie> getListCategorie() {
        return listCategorie;
    }

    public void setListCategorie(List<Categorie> listCategorie) {
        this.listCategorie = listCategorie;
    }

    public long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(long categorieId) {
        this.categorieId = categorieId;
    }
}
