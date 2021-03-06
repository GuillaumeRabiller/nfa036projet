package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.HistoriqueTemp;
import com.cnam.nfa036projet.model.Frigo;
import com.cnam.nfa036projet.model.ReleveTemp;
import com.cnam.nfa036projet.repository.FrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
     *
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
     */

    @RequestMapping(value = {"/saveFrigo"}, method = RequestMethod.POST)
    public String saveFrigo(@ModelAttribute("aFrigo") Frigo aFrigo, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || aFrigo == null) {
            return "/error";
        } else {
            frigoRepository.save(aFrigo);
            return "redirect:readFrigo";
        }
    }

    /**
     * PAGE DE UPDATE FRIGO VIA ID
     *
     * UPDATE
     *
     */

    @GetMapping("/updateFrigo/{id}")
    public String updateFrigoFormulaire(@PathVariable("id") long id, Model model) {
        Frigo frigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo id:" + id));
        model.addAttribute("aFrigo", frigo);
        return "/Frigo/updateFrigo";
    }

    @PostMapping("/updateFrigo")
    public String updateFrigo(@Valid Frigo aFrigo, BindingResult result) {
        if (result.hasErrors() || aFrigo == null) {
            return "/error";
        }
        frigoRepository.save(aFrigo);
        return "redirect:readFrigo";
    }

    /**
     * PAGE DE SUPPRESSION FRIGO VIA ID
     *
     * DELETE
     *
     */


    @GetMapping("/verifDeleteFrigo/{id}")
    public String verifDeleteFrigo(@PathVariable("id") long id, Model model) {
        Frigo aFrigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo Id:" + id));
        //Vérif si le frigo est alloué à des températures
        boolean supr ;
        if(aFrigo.getRelevesTemp().isEmpty()){
            supr = true ;
        } else {
            supr = false ;
        }
        model.addAttribute("aFrigo", aFrigo);
        model.addAttribute("supr", supr);
        return "/Frigo/deleteFrigo";
    }

    @GetMapping("/deleteFrigo/{id}")
    public String deleteFrigo(@PathVariable("id") long id, Model model) {
        Frigo aFrigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo Id:" + id));
        if(aFrigo != null) {
            frigoRepository.delete(aFrigo);
            List<Frigo> frigoList = frigoRepository.findAll();
            model.addAttribute("frigoList", frigoList);
            return "/Frigo/readFrigo";
        } else {
            return "/error";
        }
    }

    /**
     * PAGE D HISTORIQUE DE TEMPERATURE D'UN FRIGO
     *
     */

    @GetMapping("/historiqueFrigo/{id}")
    public String historiqueFrigo(@PathVariable("id") long id, Model model) {
        Frigo frigo = frigoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid frigo id:" + id));
        model.addAttribute("nomFrigo", frigo.getNomFrigo());
        List<HistoriqueTemp> historiqueList = new ArrayList<>();
        for (ReleveTemp releve: frigo.getRelevesTemp()) {
            HistoriqueTemp historique = new HistoriqueTemp();
            historique.setDate(releve.getDateEnregTemp().toLocalDate());
            historique.setHeure(releve.getDateEnregTemp().toLocalTime().truncatedTo(ChronoUnit.MINUTES));
            historique.setTemp(releve.getTemperature());
            historique.setUtilisateur(releve.getNomUtilisateur());
            historiqueList.add(historique) ;
        }
        historiqueList.sort((d1,d2) -> d1.getDate().compareTo(d2.getDate()));

        model.addAttribute("tempList", historiqueList) ;
        return "/Frigo/historiqueFrigo";
    }


}
