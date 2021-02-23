package com.cnam.nfa036projet.form;

import java.time.LocalDate;
import java.time.LocalTime;

public class HistoriqueTemp {

    private LocalDate date ;
    private LocalTime heure ;
    private float temp ;
    private String utilisateur ;

    public HistoriqueTemp() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
