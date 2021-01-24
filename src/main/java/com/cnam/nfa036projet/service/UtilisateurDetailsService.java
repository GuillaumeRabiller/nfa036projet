package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.model.Utilisateur;
import com.cnam.nfa036projet.model.UtilisateurDetails;
import com.cnam.nfa036projet.repository.StatutRepository;
import com.cnam.nfa036projet.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UtilisateurDetailsService implements UserDetailsService {

    @Autowired
    UtilisateurRepository utilisateurRepository;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException{
        Optional<Utilisateur> user = utilisateurRepository.findByLogin(name);

        user.orElseThrow(()-> new UsernameNotFoundException("Not found : " + name));

        return user.map(UtilisateurDetails::new).get();
    }


    /*
     *Méthode pour récupérer le nom + prénom de l'Utilisateur connecté
     *
     */

    public static String getNomUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UtilisateurDetails) {
            return ((UtilisateurDetails)principal).getNomPrenom();
        } else {
            return principal.toString();
        }
    }


}
