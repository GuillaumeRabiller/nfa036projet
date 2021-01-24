package com.cnam.nfa036projet.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
public class Frigo {

    //Définition des variables

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_FRIGO")
    private long id ;

    @Column(nullable = false, name = "NOM_FRIGO")
    @Size(min=2, max=30, message="Taille minimum de {min} et {max} au maximum")
    private String nomFrigo ;

    @Column(name = "DESC_FRIGO")
    @Size(min=2, max=60, message="Taille minimum de {min} et {max} au maximum")
    private String descFrigo ;

    @OneToMany(mappedBy = "frigo")
    private List<ReleveTemp> relevesTemp;

    public Frigo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomFrigo() {
        return nomFrigo;
    }

    public void setNomFrigo(String nomFrigo) {
        this.nomFrigo = nomFrigo;
    }

    public String getDescFrigo() {
        return descFrigo;
    }

    public void setDescFrigo(String descFrigo) {
        this.descFrigo = descFrigo;
    }

    public List<ReleveTemp> getRelevesTemp() {
        return relevesTemp;
    }

    public void addReleveTemp(ReleveTemp releveTemp) {
        releveTemp.setFrigo(this);
        relevesTemp.add(releveTemp);
    }
}
