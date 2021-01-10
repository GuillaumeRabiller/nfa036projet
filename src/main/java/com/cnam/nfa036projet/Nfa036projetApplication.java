package com.cnam.nfa036projet;

import com.cnam.nfa036projet.model.Produit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SpringBootApplication
public class Nfa036projetApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nfa036projetApplication.class, args);
    }


}
