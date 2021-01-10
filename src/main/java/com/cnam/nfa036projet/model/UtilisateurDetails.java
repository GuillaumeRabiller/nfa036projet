package com.cnam.nfa036projet.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;


public class UtilisateurDetails implements UserDetails {

   private Utilisateur user ;

   public UtilisateurDetails(Utilisateur user){
       this.user = user ;
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString()) ;
       return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNomPrenom(){
       return user.getNom() + " " + user.getPrenom();
    }
}
