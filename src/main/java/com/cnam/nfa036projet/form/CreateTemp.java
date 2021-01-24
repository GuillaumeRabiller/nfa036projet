package com.cnam.nfa036projet.form;

import com.cnam.nfa036projet.model.Frigo;

import java.util.List;

public class CreateTemp {

    private List<Frigo> frigoList ;

    private long frigoId ;

    private float temperature ;

    public CreateTemp() {
    }

    public List<Frigo> getFrigoList() {
        return frigoList;
    }

    public void setFrigoList(List<Frigo> frigoList) {
        this.frigoList = frigoList;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public long getFrigoId() {
        return frigoId;
    }

    public void setFrigoId(long frigoId) {
        this.frigoId = frigoId;
    }
}
