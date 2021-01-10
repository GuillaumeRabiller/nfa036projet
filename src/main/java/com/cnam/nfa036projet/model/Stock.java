package com.cnam.nfa036projet.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Stock {

    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_STOCK")
    private long id ;

    @Column(name = "DATE_ENTREE_STOCK", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateEntree ;

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

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDateTime dateEntree) {
        this.dateEntree = dateEntree;
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
