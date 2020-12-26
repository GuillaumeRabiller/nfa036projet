package com.cnam.nfa036projet.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Categorie {


    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CATEGORIE")
    private long id;

    @Size(min=2, max=64)
    @Column(nullable = false, name = "NOM_CATEGORIE")
    private String nomCategorie ;

    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits ;


    //CONSTRUCTEUR

    public Categorie(){
        super();
    }


    //GETTERS ET SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void addProduit(Produit produit) {
        produit.setCategorie(this);
        produits.add(produit);

    }

    //Méthodes

    public void deleteProduit(Produit produit) {
        produits.remove(produit);
    }

    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nomCategorie='" + nomCategorie + '\'' +
                '}';
    }
}
