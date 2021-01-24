package com.cnam.nfa036projet.controller;


import com.cnam.nfa036projet.model.Frigo;
import com.cnam.nfa036projet.repository.FrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class FrigoController {

    //Création d'un FrigoRepository

    @Autowired
    private FrigoRepository frigoRepository ;


    /**
     * LISTE DES FRIGOS EN BASE DE DONNEE
     *
     * READ
     * <p>
     */

    @GetMapping("/readFrigo")
    public String readFrigo(Model model) {
        List<Frigo> frigoList = frigoRepository.findAll();
        model.addAttribute("frigoList", frigoList);
        return "/Frigo/readFrigo";
    }

    /**
     * FORMULAIRE DE SAISIE D'UN NOUVEAU FRIGO
     *
     * CREATE
     *
     */

    @RequestMapping(value = {"/createFrigo"}, method = RequestMethod.GET)
    public String createFrigo(Model model) {
        Frigo aFrigo = new Frigo();
        model.addAttribute("aFrigo", aFrigo);
        return "/Frigo/createFrigo";
    }

    /**
     * SAUVEGARDE FORMULAIRE NOUVEAU FRIGO
     *
     * SAVE
     *
     * A TEST
     */

    @RequestMapping(value = {"/saveFrigo"}, method = RequestMethod.POST)
    public String saveFrigo(@ModelAttribute("aFrigo") Frigo aFrigo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("aFrigo", aFrigo);
            return "/Frigo/createFrigo";
        } else {

            frigoRepository.save(aFrigo);

            List<Frigo> frigoList = frigoRepository.findAll();
            model.addAttribute("frigoList", frigoList);
            return "/Frigo/readFrigo";
        }
    }

    /**
     * PAGE DE UPDATE FRIGO VIA ID
     *
     * UPDATE
     *
     * A TESTER
     */

    @GetMapping("/updateFrigo/{id}")
    public String updateFrigoFormulaire(@PathVariable("id") long id, Model model) {
        Frigo frigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo id:" + id));
        model.addAttribute("aFrigo", frigo);
        return "/Frigo/updateFrigo";
    }

    @PostMapping("/updateFrigo")
    public String updateFrigo(@Valid Frigo aFrigo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("frigotList", frigoRepository.findAll());
            return "/Frigo/readFrigo";
        }
        Frigo frigo = frigoRepository.findById(aFrigo.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid frigo id"));
        frigo.setNomFrigo(aFrigo.getNomFrigo());
        frigo.setDescFrigo(aFrigo.getDescFrigo());
        frigoRepository.save(frigo);

        model.addAttribute("frigoList", frigoRepository.findAll());
        return "/Frigo/readFrigo";
    }

    /**
     * PAGE DE SUPPRESSION FRIGO VIA ID
     *
     * DELETE
     *
     * A TESTER
     */
/*
    @GetMapping("/deleteFrigo/{id}")
    public String deleteFrigo(@PathVariable("id") long id, Model model) {
        Frigo aFrigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo Id:" + id));
        aFrigo.getCategorie().deleteProduit(aProduit);
        produitRepository.delete(aProduit);
        model.addAttribute("frigoList", frigoRepository.findAll());
        return "/Frigo/readFrigo";
    }
*/
}