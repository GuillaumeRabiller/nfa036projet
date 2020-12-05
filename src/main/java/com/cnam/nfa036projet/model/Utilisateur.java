package com.cnam.nfa036projet.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;



@Entity
public class Utilisateur {


    //DEFINITION DES VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @Size(min=2, max=30, message="Taille minimum de {min} et {max} au maximum")
    private String nom;

    @Column(nullable = false)
    @Size(min=2, max=30, message="Taille minimum de {min} et {max} au maximum")
    private String prenom;

    @Column(unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min=6, max=15)
    private String login ;

    @Column(nullable = false)
    private String password ;

    @Column(nullable = true)
    private RoleUtilisateur role ;


    //CONSTRUCTEURS

    public Utilisateur() {
        super();
    }

    public Utilisateur(String nom, String prenom, String email, String login, RoleUtilisateur role ) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.login = login;
        this.role = role;
    }


    //GETTERS ET SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public RoleUtilisateur getRole() { return role; }

    public void setRole(RoleUtilisateur role) { this.role = role; }


    //REDEFINITION TOSTRING

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", login=" + login +  '\'' +
                ", role=" + role +
                '}';
    }
}
