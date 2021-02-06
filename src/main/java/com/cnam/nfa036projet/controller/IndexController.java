package com.cnam.nfa036projet.controller;

import com.cnam.nfa036projet.form.CreateTemp;
import com.cnam.nfa036projet.repository.FrigoRepository;
import com.cnam.nfa036projet.service.FrigoService;
import com.cnam.nfa036projet.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    private FrigoService frigoService ;

    @Autowired
    private StockService stockService ;

    @Autowired
    private FrigoRepository frigoRepository;


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
     * PAGE INDEX AVEC TABLEAU DE BORD
     *
     * READ
     *
     *
     */

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("stockList", stockService.listeStock());
        model.addAttribute("tempList", frigoService.lastTempList());

        //Gestion du popup renseignement temp
        CreateTemp aTemp = new CreateTemp();
        aTemp.setFrigoList(frigoRepository.findAll());
        model.addAttribute("aTemp", aTemp);

        return "/index";
    }



}
