package com.cnam.nfa036projet.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn (name = "ID_UTILISATEUR")
    private Utilisateur utilisateur ;


    //CONSTRUCTEURS

    public Produit() {
        super();
    }

    public Produit(@Size(min = 2, max = 64) String nomProduit, int dureeConservation, LocalDate dateCreation, Categorie categorie, Utilisateur utilisateur) {
        this.nomProduit = nomProduit;
        this.dureeConservation = dureeConservation;
        this.dateCreation = LocalDate.now() ;
        this.categorie = categorie;
        this.utilisateur = utilisateur;
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
                ", utilisateur=" + utilisateur.getNom() +
                '}';
    }
}
