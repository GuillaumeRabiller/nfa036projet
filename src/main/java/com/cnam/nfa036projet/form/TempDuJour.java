package com.cnam.nfa036projet.form;

public class TempDuJour {

    private String nomFrigo ;

    private float tempMatin = -100.0f ;

    private float tempAprem = -100.0f ;

    public TempDuJour() {
    }

    public String getNomFrigo() {
        return nomFrigo;
    }

    public void setNomFrigo(String nomFrigo) {
        this.nomFrigo = nomFrigo;
    }

    public float getTempMatin() {
        return tempMatin;
    }

    public void setTempMatin(float tempMatin) {
        this.tempMatin = tempMatin;
    }

    public float getTempAprem() {
        return tempAprem;
    }

    public void setTempAprem(float tempAprem) {
        this.tempAprem = tempAprem;
    }
}

