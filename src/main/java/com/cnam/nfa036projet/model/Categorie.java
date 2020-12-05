package com.cnam.nfa036projet.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Categorie {


    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min=2, max=64)
    @Column(nullable = false)
    private String nomCategorie ;


    //CONSTRUCTEUR

    public Categorie(){
        super();
    }


    //GETTERS ET SETTERS

    public Categorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

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


    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nomCategorie='" + nomCategorie + '\'' +
                '}';
    }
}
