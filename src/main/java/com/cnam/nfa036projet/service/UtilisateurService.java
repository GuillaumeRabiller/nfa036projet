package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.model.UtilisateurDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtilisateurService {

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

}
