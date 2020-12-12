package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.model.Produit;
import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.repository.ProduitRepository;

import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ProduitController {

    //Création d'un ProduitRepository

    private ProduitRepository produitRepository ;

    //Création d'un utilisateurRepository (pour forcer un utilisateur à la création d'un produit)

    private UtilisateurRepository utilisateurRepository ;

    /**
     * LISTE DES PRODUITS EN BASE DE DONNEE
     *
     * READ
     * <p>
     * A TEST
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
        //envoi des infos Utilisateur 1 ADMIN (valeur forcée sur lui pour le moment)
        model.addAttribute("utilisateur", utilisateurRepository.findById((long)1));
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

            //ajout en dur de l'utilisateur qui créé le produit
            Utilisateur utilisateur = utilisateurRepository.getOne((long)1);
            aProduit.setUtilisateur(utilisateur);

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
        Produit aProduit = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid produit id:" + id));

        model.addAttribute("aProduit", aProduit);
        return "/Produit/updateProduit";
    }

    @PostMapping("/updateProduit")
    public String updateproduit(@Valid Produit aProduit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/produit/updateProduit";
        }
        produitRepository.save(aProduit);
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
        produitRepository.delete(aProduit);
        model.addAttribute("produitList", produitRepository.findAll());
        return "/Produit/readProduit";
    }

}
