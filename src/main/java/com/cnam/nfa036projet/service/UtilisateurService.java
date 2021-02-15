package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.UpdateUtilForm;
import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.model.UtilisateurDetails;
import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository ;

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

    public Utilisateur updateUser(UpdateUtilForm aUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(aUser.getid()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        utilisateur.setNom(aUser.getNom());
        utilisateur.setPrenom(aUser.getPrenom());
        utilisateur.setEmail(aUser.getEmail());
        utilisateur.setLogin(aUser.getLogin());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(aUser.getPassword(), utilisateur.getPassword())){
            String hashedPassword = passwordEncoder.encode(aUser.getPassword());
            utilisateur.setPassword(hashedPassword);
        }
        utilisateur.setRole(aUser.getRole());
        return utilisateur ;
    }

}
