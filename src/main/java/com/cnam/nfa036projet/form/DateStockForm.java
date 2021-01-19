package com.cnam.nfa036projet.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class DateStockForm {

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private String date ;

    private long id;

    public DateStockForm() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String dateStock) {
        this.date = dateStock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
