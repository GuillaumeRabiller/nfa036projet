package com.cnam.nfa036projet.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Stock {

    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id ;

    @Column(nullable = false)
    @Size(min = 2, max = 36)
    private String nomStock ;


    //CONSTUCTEURS

    public Stock(){
        super();
    }

    public Stock(long id, String nomStock) {
        this.id = id;
        this.nomStock = nomStock;
    }


    //GETTERS ET SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomStock() {
        return nomStock;
    }

    public void setNomStock(String nomStock) {
        this.nomStock = nomStock;
    }


    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", nomStock='" + nomStock + '\'' +
                '}';
    }
}
