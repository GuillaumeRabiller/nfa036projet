package com.cnam.nfa036projet.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Produit {


    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID_PRODUIT")
    private long id ;

    @Column(nullable = false, name="NOM_PRODUIT")
    @Size(min=2, max=64)
    private String nomProduit ;

    @Column(nullable = false, name="DUREE_CONSERVATION")
    private int dureeConservation ;

    @Column(nullable = false, name="DATE_CREATION")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate dateCreation ;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIE")
    private Categorie categorie ;

    @Transient
    private String categorieId ;

    @Column (name = "UTILISATEUR")
    private String utilisateur ;

    @OneToMany(mappedBy = "produit")
    private List<Stock> stocks ;


    //CONSTRUCTEURS

    public Produit() {
    }


    //GETTERS ET SETTERS

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

    public LocalDate getDateCreation() { return dateCreation; }

    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(String categorieId) {
        this.categorieId = categorieId;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void addStock(Stock stock) {
        stock.setProduit(this);
        stocks.add(stock);
    }

    //Méthodes

    public void changeCategorie(Categorie newCategorie) {
        this.getCategorie().deleteProduit(this);
        newCategorie.addProduit(this);
    }


    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nomProduit='" + nomProduit + '\'' +
                ", dureeConservation=" + dureeConservation +
                ", dateCreation=" + dateCreation +
                ", categorie=" + categorie.getNomCategorie() +
                ", utilisateur=" + utilisateur +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produit)) return false;
        Produit produit = (Produit) o;
        return id == produit.id && com.google.common.base.Objects.equal(nomProduit, produit.nomProduit) && com.google.common.base.Objects.equal(categorie, produit.categorie);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, nomProduit, categorie);
    }
}
