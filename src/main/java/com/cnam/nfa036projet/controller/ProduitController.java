package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.UpdateProduitForm;
import com.cnam.nfa036projet.model.Categorie;
import com.cnam.nfa036projet.model.Produit;
import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.model.UtilisateurDetails;
import com.cnam.nfa036projet.repository.CategorieRepository;
import com.cnam.nfa036projet.repository.ProduitRepository;

import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Controller
public class ProduitController {

    //Création d'un ProduitRepository

    @Autowired
    private ProduitRepository produitRepository ;

    //Création d'un utilisateurRepository (pour forcer un utilisateur à la création d'un produit)

    @Autowired
    private UtilisateurRepository utilisateurRepository ;

    //Création d'un categorieRepository

    @Autowired
    private CategorieRepository categorieRepository ;

    /*
     *Méthode pour récupérer le nom + prénom de l'Utilisateur connecté
     *
     */

    public String getNomUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UtilisateurDetails) {
            return ((UtilisateurDetails)principal).getNomPrenom();
        } else {
            return principal.toString();
        }
    }


    /**
     * LISTE DES PRODUITS EN BASE DE DONNEE
     *
     * READ
     * <p>
     */

    @GetMapping("/readProduit")
    public String readProduit(Model model) {
        List<Produit> produitList = produitRepository.findAll();
        model.addAttribute("produitList", produitList);
        return "/Produit/readProduit";
    }

    /**
     * FORMULAIRE DE SAISIE D'UN NOUVEAU PRODUIT
     *
     * CREATE
     *
     * A TEST
     */

    @RequestMapping(value = {"/createProduit"}, method = RequestMethod.GET)
    public String createProduit(Model model) {
        Produit aProduit = new Produit();
        model.addAttribute("aProduit", aProduit);
        List<Categorie> categorieList = categorieRepository.findAll() ;
        model.addAttribute("categorieList", categorieList);
        return "/Produit/createProduit";
    }

    /**
     * SAUVEGARDE FORMULAIRE NOUVEAU PRODUIT
     *
     * SAVE
     *
     * A TEST
     */

    @RequestMapping(value = {"/saveProduit"}, method = RequestMethod.POST)
    public String saveProduit(@ModelAttribute("aProduit") Produit aProduit, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("aProduit", aProduit);
            return "/Produit/createProduit";
        } else {
            aProduit.setDateCreation(LocalDate.now());

            aProduit.setUtilisateur(getNomUser());

            //Recupération de la categorie
            Optional<Categorie> cat = categorieRepository.findById(Long.valueOf(aProduit.getCategorieId()));
            cat.ifPresent(categorie -> {
                categorie.addProduit(aProduit);
            });

            produitRepository.save(aProduit);

            List<Produit> produitList = produitRepository.findAll();

            model.addAttribute("produitList", produitList);
            return "/Produit/readProduit";
        }
    }

    /**
     * PAGE DE UPDATE PRODUIT VIA ID
     *
     * UPDATE
     *
     * A TESTER
     */

    @GetMapping("/updateProduit/{id}")
    public String updateProduitFormulaire(@PathVariable("id") long id, Model model) {
        Produit produit = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid produit id:" + id));
        List<Categorie> categorieList = categorieRepository.findAll() ;
        UpdateProduitForm aProduit = new UpdateProduitForm(produit.getId(),produit.getNomProduit(), produit.getDureeConservation(),categorieList);
        model.addAttribute("aProduit", aProduit);
        model.addAttribute("lastselected",produit.getCategorie().getId());
        return "/Produit/updateProduit";
    }

    @PostMapping("/updateProduit")
    public String updateProduit(@Valid UpdateProduitForm aProduit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("produitList", produitRepository.findAll());
            return "/Produit/readProduit";
        }
        Produit produit = produitRepository.findById(aProduit.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid produit id"));
        //Recupération de la categorie
        Optional<Categorie> cat = categorieRepository.findById(aProduit.getCategorieId());
        cat.ifPresent(categorie -> {
            produit.changeCategorie(categorie);
        });
        produit.setNomProduit(aProduit.getNomProduit());
        produit.setDureeConservation(aProduit.getDureeConservation());
        produitRepository.save(produit);

        model.addAttribute("produitList", produitRepository.findAll());
        return "/Produit/readProduit";
    }

    /**
     * PAGE DE SUPPRESSION PRODUIT VIA ID
     *
     * DELETE
     *
     * A TESTER
     */

    @GetMapping("/deleteProduit/{id}")
    public String deleteProduit(@PathVariable("id") long id, Model model) {
        Produit aProduit = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid produit Id:" + id));
        aProduit.getCategorie().deleteProduit(aProduit);
        produitRepository.delete(aProduit);
        model.addAttribute("produitList", produitRepository.findAll());
        return "/Produit/readProduit";
    }

}
