package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.model.Categorie;
import com.cnam.nfa036projet.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@Controller
public class CategorieController {

    //Creation du Repository
    @Autowired
    private CategorieRepository categorieRepository ;

    /**
     * LISTE DES CATEGORIES EN BASE DE DONNEE
     *
     * READ
     *
     *  FONCTIONNEL
     */

    @GetMapping("/readCategorie")
    public String readCategorie(Model model) {
        List<Categorie> categorieList = categorieRepository.findAll();
        model.addAttribute("categorieList", categorieList);
        return "/Categorie/readCategorie";
    }

    /**
     * FORMULAIRE DE SAISIE D'UNE NOUVELLE CATEGORIE
     *
     * CREATE
     *
     * FONCTIONNEL
     */

    @RequestMapping(value = {"/createCategorie"}, method = RequestMethod.GET)
    public String createCategorie(Model model) {
        Categorie aCategorie = new Categorie();
        model.addAttribute("aCategorie", aCategorie);
        return "/Categorie/createCategorie";
    }

    /**
     * SAUVEGARDE FORMULAIRE NOUVELLE CATEGORIE
     *
     * SAVE
     *
     * FONCTIONNEL
     */

    @RequestMapping(value = {"/saveCategorie"}, method = RequestMethod.POST)
    public String saveCategorie(@ModelAttribute("aCategorie") Categorie aCategorie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || aCategorie == null) {
            return "/error";
        } else {
            categorieRepository.save(aCategorie);
            return "redirect:readCategorie";
        }
    }

    /**
     * PAGE DE UPDATE CATEGORIE VIA ID
     *
     * UPDATE
     *
     * FONCTIONNEL
     */

    @GetMapping("/updateCategorie/{id}")
    public String updateCategorieFormulaire(@PathVariable("id") long id, Model model) {
        Categorie aCategorie = categorieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid categorie id:" + id));
        model.addAttribute("aCategorie", aCategorie);
        return "/Categorie/updateCategorie";
    }

    @PostMapping("/updateCategorie")
    public String updateCategorie(@Valid Categorie aCategorie, BindingResult result, Model model) {
        if (result.hasErrors() || aCategorie == null) {
            return "/error";
        }
        categorieRepository.save(aCategorie);
        return "redirect:readCategorie";
    }

    /**
     * PAGE DE SUPPRESSION CATEGORIE VIA ID
     *
     * DELETE
     *
     * FONCTIONNEL
     */

    @GetMapping("/verifDeleteCategorie/{id}")
    public String verifDeleteCategorie(@PathVariable("id") long id, Model model) {
        Categorie aCategorie = categorieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid categorie Id:" + id));
        boolean supr ;
        //Vérification si la catégorie est allouée à certains produits
        if(aCategorie.getProduits().isEmpty()){
            supr = true;
        } else {
            supr = false ;
        }
        model.addAttribute("aCategorie", aCategorie);
        model.addAttribute("supr", supr);
        return "/Categorie/deleteCategorie";
    }

    @GetMapping("/deleteCategorie/{id}")
    public String deleteCategorie(@PathVariable("id") long id, Model model) {
        Categorie aCategorie = categorieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid categorie Id:" + id));
        if(aCategorie != null) {
            categorieRepository.delete(aCategorie);
            List<Categorie> categorieList = categorieRepository.findAll();
            model.addAttribute("categorieList", categorieList);
            return "/Categorie/readCategorie";
        } else {
            return "/error";
        }
    }


}
