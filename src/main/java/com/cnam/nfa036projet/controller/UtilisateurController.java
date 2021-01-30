package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.UpdateUtilForm;
import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@Controller
public class UtilisateurController {


    //Injection via messages.properties
    @Value("${welcome.message}")
    private String welcomeMessage;

    @Value("${error.message}")
    private String errorMessage;

    //Création de l'utilisateur Repository

    @Autowired
    private UtilisateurRepository utilisateurRepository;

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
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(aUser.getPassword());
            aUser.setPassword(hashedPassword);
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
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id:" + id));
        UpdateUtilForm aUser = new UpdateUtilForm(utilisateur.getId(),utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getEmail(),utilisateur.getLogin(),utilisateur.getPassword(),utilisateur.getRole());
        model.addAttribute("aUser", aUser);
        return "/Utilisateur/updateUtil";
    }

    @PostMapping("/updateUtil")
    public String updateUtilisateur( @Valid UpdateUtilForm aUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/Utilisateur/updateUtil";
        }
        Utilisateur utilisateur = utilisateurRepository.findById(aUser.getid()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        utilisateur.setNom(aUser.getNom());
        utilisateur.setPrenom(aUser.getPrenom());
        utilisateur.setEmail(aUser.getEmail());
        utilisateur.setLogin(aUser.getLogin());
        utilisateur.setPassword(aUser.getPassword());
        utilisateur.setRole(aUser.getRole());
        utilisateurRepository.save(utilisateur);
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