package com.cnam.nfa036projet.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Stock {

    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_STOCK")
    private long id ;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @Column(name = "DATE_ENTREE_STOCK", nullable = false)
    private LocalDate dateEntree ;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @Column(name = "DATE_SORTIE_STOCK")
    private LocalDate dateSortie ;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUIT")
    private Produit produit ;

    @ManyToOne
    @JoinColumn(name = "ID_STATUT")
    private Statut statut ;


    //CONSTUCTEURS

    public Stock(){
        super();
    }




    //GETTERS ET SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    //Méthodes

    public void changeStatut(Statut newStatut) {
        this.getStatut().deleteStock(this);
        newStatut.addStock(this);
    }


}
