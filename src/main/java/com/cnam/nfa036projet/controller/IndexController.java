package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;


@Controller
public class IndexController {


    //Injection via messages.properties
    @Value("${welcome.message}")
    private String welcomeMessage;

    @Value("${error.message}")
    private String errorMessage;

    //Création de l'utilisateur Repository

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Cette méthode permet de capter un GET de la page
     * http://localhost:8080/ ou http://localhost:8080/index
     *
     * @param model
     * @return PAGE INDEX, AVEC LES DIVERS LIENS
     */

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("msg_accueil", welcomeMessage);
        return "index";
    }

    /**
     * SPRING SECURITY
     *
     * LOGIN PAGE
     *
     * NON UTILISE POUR LE MOMENT
     */

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "login";
    }


    /**
     * LISTE DES UTILISATEURS EN BASE DE DONNEE
     *
     * READ
     * <p>
     * FONCTIONNEL
     */

    @GetMapping("/readUtil")
    public String readUtilisateur(Model model) {
        List<Utilisateur> userList = utilisateurRepository.findAll();
        model.addAttribute("userList", userList);
        return "/Utilisateur/readUtil";
    }

    /**
     * FORMULAIRE DE SAISIE D'UN NOUVEL UTILISATEUR
     *
     * CREATE
     *
     * FONCTIONNEL
     */

    @RequestMapping(value = {"/createUtil"}, method = RequestMethod.GET)
    public String createUtilisateur(Model model) {
        Utilisateur aUser = new Utilisateur();
        model.addAttribute("aUser", aUser);
        return "/Utilisateur/createUtil";
    }

    /**
     * SAUVEGARDE FORMULAIRE NOUVEL UTILISATEUR
     *
     * SAVE
     *
     * FONCTIONNEL
     */

    @RequestMapping(value = {"/saveUtil"}, method = RequestMethod.POST)
    public String saveUtilisateur(@ModelAttribute("aUser") Utilisateur aUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("aUser", aUser);
            return "/Utilisateur/createUtil";
        } else {
            utilisateurRepository.save(aUser);
            List<Utilisateur> userList = utilisateurRepository.findAll();
            model.addAttribute("userList", userList);
            return "/Utilisateur/readUtil";
        }
    }

    /**
     * PAGE DE UPDATE UTILISATEUR VIA ID
     *
     * UPDATE
     *
     * FONCTIONNEL
     */

    @GetMapping("/updateUtil/{id}")
    public String updateUtilisateurFormulaire(@PathVariable("id") long id, Model model) {
        Utilisateur aUser = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id:" + id));

        model.addAttribute("aUser", aUser);
        return "/Utilisateur/updateUtil";
    }

    @PostMapping("/updateUtil")
    public String updateUtilisateur( @Valid Utilisateur aUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/Utilisateur/updateUtil";
        }
        utilisateurRepository.save(aUser);
        model.addAttribute("userList", utilisateurRepository.findAll());
        return "/Utilisateur/readUtil";
    }

    /**
     * PAGE DE SUPPRESSION UTILISATEUR VIA ID
     *
     * DELETE
     *
     * FONCTIONNEL
     */

    @GetMapping("/deleteUtil/{id}")
    public String deleteUtilisateur(@PathVariable("id") long id, Model model) {
        Utilisateur aUser = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        utilisateurRepository.delete(aUser);
        model.addAttribute("userList", utilisateurRepository.findAll());
        return "/Utilisateur/readUtil";
    }

}