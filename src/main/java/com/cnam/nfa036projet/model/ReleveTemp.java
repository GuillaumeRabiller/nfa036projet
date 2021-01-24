package com.cnam.nfa036projet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ReleveTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_STOCK")
    private long id ;

    @Column(name = "DATE_ENREG_TEMP", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateEnregTemp ;

    @Column(name = "TEMPERATURE", nullable = false)
    private float temperature ;

    @Column(name = "NOM_UTILISATEUR")
    private String nomUtilisateur ;

    @ManyToOne
    @JoinColumn(name="ID_FRIGO")
    private Frigo frigo ;


    public ReleveTemp() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateEnregTemp() {
        return dateEnregTemp;
    }

    public void setDateEnregTemp(LocalDateTime dateEnregTemp) {
        this.dateEnregTemp = dateEnregTemp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public Frigo getFrigo() {
        return frigo;
    }

    public void setFrigo(Frigo frigo) {
        this.frigo = frigo;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }
}
