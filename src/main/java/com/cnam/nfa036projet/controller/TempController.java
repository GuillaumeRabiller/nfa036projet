package com.cnam.nfa036projet.controller;


import com.cnam.nfa036projet.form.CreateTemp;
import com.cnam.nfa036projet.model.*;
import com.cnam.nfa036projet.repository.FrigoRepository;
import com.cnam.nfa036projet.repository.ReleveTempRepository;
import com.cnam.nfa036projet.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class TempController {

    @Autowired
    FrigoRepository frigoRepository ;

    @Autowired
    ReleveTempRepository releveTempRepository ;

    @Autowired
    UtilisateurService userService ;


    @RequestMapping(value = {"/createTemp"}, method = RequestMethod.GET)
    public String createTemp(Model model) {
        CreateTemp aTemp = new CreateTemp();
        aTemp.setFrigoList(frigoRepository.findAll());
        model.addAttribute("aTemp", aTemp);
        return "/Temp/createTemp";
    }


    /**
     * SAUVEGARDE FORMULAIRE TEMPERATURE
     *
     * SAVE
     *
     */

    @RequestMapping(value = {"/saveTemp"}, method = RequestMethod.POST)
    public String saveTemp(@ModelAttribute("aTemp") CreateTemp aTemp, BindingResult bindingResult, Model model)  {
        if (bindingResult.hasErrors()) {
            createTemp(model);
        } else {
            ReleveTemp temp = new ReleveTemp();
            temp.setDateEnregTemp(LocalDateTime.now());
            Optional<Frigo> frigo = frigoRepository.findById(aTemp.getFrigoId());
            frigo.ifPresent(fridge -> {
                fridge.addReleveTemp(temp);
            });
            temp.setTemperature(aTemp.getTemperature());
            temp.setNomUtilisateur(userService.getNomUser());

            releveTempRepository.save(temp);


        }

        return "redirect:index";
    }



}
