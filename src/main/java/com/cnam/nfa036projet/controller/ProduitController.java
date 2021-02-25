package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.UpdateProduitForm;
import com.cnam.nfa036projet.model.Categorie;
import com.cnam.nfa036projet.model.Produit;
import com.cnam.nfa036projet.repository.CategorieRepository;
import com.cnam.nfa036projet.repository.ProduitRepository;
import com.cnam.nfa036projet.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@Controller
public class ProduitController {

    //Création des Repository et Services

    @Autowired
    private ProduitRepository produitRepository ;

    @Autowired
    private CategorieRepository categorieRepository ;

    @Autowired
    private ProduitService produitService;

    /**
     * LISTE DES PRODUITS EN BASE DE DONNEE
     *
     * READ
     *
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
     *
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
     */

    @RequestMapping(value = {"/saveProduit"}, method = RequestMethod.POST)
    public String saveProduit(@ModelAttribute("aProduit") Produit aProduit, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || aProduit == null) {
            return "/error";
        } else {
            Produit aProduct = produitService.createProd(aProduit) ;
            produitRepository.save(aProduct);
            return "redirect:readProduit";
        }
    }

    /**
     * PAGE DE UPDATE PRODUIT VIA ID
     *
     * UPDATE
     *
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
    public String updateProduit(@Valid UpdateProduitForm aProduit, BindingResult result) {
        if (result.hasErrors() || aProduit == null) {
            return "/error";
        }
        Produit produit = produitService.changeProd(aProduit);
        produitRepository.save(produit);
        return "redirect:readProduit";
    }

    /**
     * PAGE DE SUPPRESSION PRODUIT VIA ID
     *
     * DELETE
     *
     */

    @GetMapping("/verifDeleteProduit/{id}")
    public String verifDeleteProduit(@PathVariable("id") long id, Model model) {
        Produit aProduit = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid produit Id:" + id));
        //Vérification si le produit est alloué à un stock
        boolean supr ;
        if(aProduit.getStocks().isEmpty()){
            supr = true ;
        } else {
            supr = false ;
        }
        model.addAttribute("aProduit", aProduit) ;
        model.addAttribute("supr", supr);
        return "/Produit/deleteProduit";
    }

    @GetMapping("/deleteProduit/{id}")
    public String deleteProduit(@PathVariable("id") long id, Model model) {
        Produit aProduit = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid produit Id:" + id));
        if(aProduit != null){
            aProduit.getCategorie().deleteProduit(aProduit);
            produitRepository.delete(aProduit);
            model.addAttribute("produitList", produitRepository.findAll());
            return "/Produit/readProduit";
        } else {
            return "/error";
        }
    }

}
