package com.cnam.nfa036projet.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Statut {

    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_STATUT")
    private long id;

    @Size(min=2, max=64)
    @Column(nullable = false, name = "NOM_STATUT")
    private String nomStatut ;

    @OneToMany(mappedBy = "statut")
    private List<Stock> stocks ;


    //CONSTRUCTEUR

    public Statut(){
        super();
    }


    //GETTERS ET SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomStatut() {
        return nomStatut;
    }

    public void setNomStatut(String nomStatut) {
        this.nomStatut = nomStatut;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void addStock(Stock stock) {
        stock.setStatut(this);
        stocks.add(stock);

    }


    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Statut{" +
                "id=" + id +
                ", nomStatut='" + nomStatut + '\'' +
                '}';
    }

    //Méthodes

    public void deleteStock(Stock stock) {
        stocks.remove(stock);
    }
}
