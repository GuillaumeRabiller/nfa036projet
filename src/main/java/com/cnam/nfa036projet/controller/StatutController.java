package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.model.Statut;
import com.cnam.nfa036projet.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


/**
 *
 * CONTROLLER DESACTIVÉ, IL SERT EN DEBUT DE PRODUCTION POUR CREER LES DIFFERENTS STATUTS
 * UNE FOIS LES STATUTS CRÉÉS, IL NE FAUT PLUS Y TOUCHER
 *
 * LISTE DES STATUTS A CREER :
 *
 * En Stock
 * A Contrôler
 * Retiré
 * Erreur
 * Consommé
 *
 */

@Controller
public class StatutController {

    //Creation du Repository
    @Autowired
    private StatutRepository statutRepository ;

    /**
     * LISTE DES STATUTS EN BASE DE DONNEE
     *
     * READ
     *
     */

    @GetMapping("/readStatut")
    public String readStatut(Model model) {
        List<Statut> statutList = statutRepository.findAll();
        model.addAttribute("statutList", statutList);
        return "/Statut/readStatut";
    }

    /**
     * FORMULAIRE DE SAISIE D'UN NOUVEAU STATUT
     *
     * CREATE
     *
     */

    @RequestMapping(value = {"/createStatut"}, method = RequestMethod.GET)
    public String createStatut(Model model) {
        Statut aStatut = new Statut();
        model.addAttribute("aStatut", aStatut);
        return "/Statut/createStatut";
    }

    /**
     * SAUVEGARDE FORMULAIRE NOUVEAU STATUT
     *
     * SAVE
     *
     */

    @RequestMapping(value = {"/saveStatut"}, method = RequestMethod.POST)
    public String saveStatut(@ModelAttribute("aStatut") Statut aStatut, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("aStatut", aStatut);
            return "/Statut/createStatut";
        } else {
            statutRepository.save(aStatut);
            List<Statut> statutList = statutRepository.findAll();
            model.addAttribute("statutList", statutList);
            return "/Statut/readStatut";
        }
    }

    /**
     * PAGE DE UPDATE STATUT VIA ID
     *
     * UPDATE
     *
     */

    @GetMapping("/updateStatut/{id}")
    public String updateStatutFormulaire(@PathVariable("id") long id, Model model) {
        Statut aStatut = statutRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid statut id:" + id));

        model.addAttribute("aStatut", aStatut);
        return "/Statut/updateStatut";
    }

    @PostMapping("/updateStatut")
    public String updateStatut(@Valid Statut aStatut, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/Statut/updateStatut";
        }
        statutRepository.save(aStatut);
        model.addAttribute("statutList", statutRepository.findAll());
        return "/Statut/readStatut";
    }

    /**
     * PAGE DE SUPPRESSION STATUT VIA ID
     *
     * DELETE
     *
     */

    @GetMapping("/deleteStatut/{id}")
    public String deleteStatut(@PathVariable("id") long id, Model model) {
        Statut aStatut = statutRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid statut Id:" + id));
        statutRepository.delete(aStatut);
        model.addAttribute("statutList", statutRepository.findAll());
        return "/Statut/readStatut";
    }


}
