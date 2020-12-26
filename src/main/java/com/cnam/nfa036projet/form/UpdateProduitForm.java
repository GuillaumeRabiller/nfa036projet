package com.cnam.nfa036projet.form;

import com.cnam.nfa036projet.model.Categorie;

import java.util.List;

public class UpdateProduitForm {

    //Définition des variables

    private long id ;
    private String nomProduit ;
    private int dureeConservation ;
    private List<Categorie> categories ;
    private long categorieId ;


    //Constructeur

    public UpdateProduitForm(long id, String nomProduit, int dureeConservation, List<Categorie> categories) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.dureeConservation = dureeConservation;
        this.categories = categories;
    }

    //Getters et Setters

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

    public int getDureeConservation() {
        return dureeConservation;
    }

    public void setDureeConservation(int dureeConservation) {
        this.dureeConservation = dureeConservation;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(long categorieId) {
        this.categorieId = categorieId;
    }
}
