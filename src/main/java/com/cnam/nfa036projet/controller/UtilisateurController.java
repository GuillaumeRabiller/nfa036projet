package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.UpdateUtilForm;
import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.repository.UtilisateurRepository;
import com.cnam.nfa036projet.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@Controller
public class UtilisateurController {


    //Création de l'utilisateur Repository

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JavaMailSender emailSender ;

    /**
     * LISTE DES UTILISATEURS EN BASE DE DONNEE
     *
     * READ
     *
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
    public String saveUtilisateur(@ModelAttribute("aUser") Utilisateur aUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || aUser == null) {
            return "/error";
        } else {
            //Encodage du mot de passe
            String pass = aUser.getPassword() ;  // sauvegarde du password non encodé pour l'envoi en mail
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(aUser.getPassword());
            aUser.setPassword(hashedPassword);
            //Sauvegarde
            utilisateurRepository.save(aUser);

            //Envoi du mail à l'utilisateur avec ses identifiants
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(aUser.getEmail());
            message.setSubject("Envoi de vos identifiants Logiciel Traçage Produits Restaurant");
            message.setText("Bonjour, voici vos nouveaux identifiants de connexion : \n Login = " + aUser.getLogin() + "\n Mot de passe = " + pass
            + "\n Vous serez connectez en tant que : " + aUser.getRole().toString());
            this.emailSender.send(message);

            return "redirect:readUtil";
        }
    }

    /**
     * PAGE DE UPDATE UTILISATEUR VIA ID
     *
     * UPDATE
     *
     */

    @GetMapping("/updateUtil/{id}")
    public String updateUtilisateurFormulaire(@PathVariable("id") long id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id:" + id));
        UpdateUtilForm aUser = new UpdateUtilForm(utilisateur.getId(),utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getEmail(),utilisateur.getLogin(),utilisateur.getPassword(),utilisateur.getRole());
        model.addAttribute("aUser", aUser);
        return "/Utilisateur/updateUtil";
    }

    @PostMapping("/updateUtil")
    public String updateUtilisateur( @Valid UpdateUtilForm aUser, BindingResult result) {
        if (result.hasErrors() || aUser == null) {
            return "/error";
        }
        Utilisateur utilisateur = utilisateurService.updateUser(aUser);
        utilisateurRepository.save(utilisateur);
        return "redirect:readUtil";
    }

    /**
     * PAGE DE SUPPRESSION UTILISATEUR VIA ID
     *
     * DELETE
     *
     */

    @GetMapping("/verifDeleteUtil/{id}")
    public String verifDeleteUtilisateur(@PathVariable("id") long id, Model model) {
        Utilisateur aUser = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("aUser", aUser) ;
        return "/Utilisateur/deleteUtil";
    }

    @GetMapping("/deleteUtil/{id}")
    public String deleteUtilisateur(@PathVariable("id") long id, Model model) {
        Utilisateur aUser = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if(aUser != null){
            utilisateurRepository.delete(aUser);
            model.addAttribute("userList", utilisateurRepository.findAll());
            return "/Utilisateur/readUtil";
        } else {
            return "/error";
        }
    }

}