package com.cnam.nfa036projet.controller;


import com.cnam.nfa036projet.form.*;
import com.cnam.nfa036projet.model.*;
import com.cnam.nfa036projet.repository.ProduitRepository;
import com.cnam.nfa036projet.repository.StatutRepository;
import com.cnam.nfa036projet.repository.StockHistoriqueRepository;
import com.cnam.nfa036projet.repository.StockRepository;
import com.cnam.nfa036projet.service.StockService;
import com.cnam.nfa036projet.service.UtilisateurDetailsService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class StockController {

    //Création d'un StockRepository

    @Autowired
    private StockRepository stockRepository ;

    @Autowired
    private ProduitRepository produitRepository ;

    @Autowired
    private StatutRepository statutRepository ;

    @Autowired
    private StockHistoriqueRepository stockHistoriqueRepository ;

    private StockService stockService = new StockService() ;

    public Statut findByNomStatut(String nomStatut){
        Statut statut = statutRepository.findByNomStatut(nomStatut);
        return statut ;
    }

    /*
     *Méthode pour récupérer le nom + prénom de l'Utilisateur connecté
     *
     */
/*
    public String getNomUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UtilisateurDetails) {
            return ((UtilisateurDetails)principal).getNomPrenom();
        } else {
            return principal.toString();
        }
    }
*/
    //Méthode d'envoi de la Liste de Produits en Stock, étant appelée après chaque opération

    public List<StockForm> listeStock() {
        List<Stock> stocks = stockRepository.findAll();
        List<StockForm> stockList = new ArrayList<>();
        for (Stock stock:stocks) {

                //MISE A JOUR DU STATUT SI DLC COURTE = INFERIEUR A 24 HEURES + SAUVEGARDE NV STATUT EN BASE + SAUVEGARDE HISTORIQUE
                LocalDateTime dateEntree = stock.getDateEntree().truncatedTo(ChronoUnit.SECONDS);
                LocalDateTime dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
                dlc.truncatedTo(ChronoUnit.SECONDS);
                long daysBetween = LocalDateTime.now().until(dlc, ChronoUnit.HOURS);
                if (stock.getStatut().getNomStatut().equals("En Stock") && daysBetween <= 24) {
                    Statut statut = findByNomStatut("A Contrôler");
                    stock.changeStatut(statut);
                    stockRepository.save(stock);

                    // AJOUT D'UN NOUVEL HISTORIQUE
                    StockHistorique historique = new StockHistorique();
                    historique.setDateMouvementStock(LocalDateTime.now());
                    historique.setProduit(stock.getProduit().getNomProduit());
                    historique.setIdProduit(stock.getId());
                    historique.setUtilisateur(UtilisateurDetailsService.getNomUser());
                    historique.setStatut(statut.getNomStatut());
                    historique.setCategorie(stock.getProduit().getCategorie().getNomCategorie());
                    stockHistoriqueRepository.save(historique);
                }

                //On renseigne stockForm
                long id = stock.getId();
                String nomProduit = stock.getProduit().getNomProduit();
                String categorie = stock.getProduit().getCategorie().getNomCategorie();
                String statut = stock.getStatut().getNomStatut();
                int nbStatut = 0 ;
                if (daysBetween > 24) {
                    nbStatut = 1;
                } else if (daysBetween >= 0) {
                    nbStatut = 2;
                } else nbStatut = 3;

                StockForm aStock = new StockForm(id, nomProduit, categorie, dateEntree, dlc, statut, nbStatut);
                stockList.add(aStock);
        }
        return stockList ;
    }

    //HISTORIQUE DES STOCKS SELON UNE DATE

    public List<HistoriqueForm> historiqueStock(LocalDate date) {
        List<StockHistorique> stocks = stockHistoriqueRepository.findAll();
        List<HistoriqueForm> stockList = new ArrayList<>();
        for (StockHistorique stock:stocks) {
            //renseignement de la liste selon la date
            if( stock.getDateMouvementStock().toLocalDate().isEqual(date) )
            {
                long idProduit = stock.getIdProduit();
                String nomProduit = stock.getProduit();
                String categorie = stock.getCategorie();
                String statut = stock.getStatut();
                String utilisateur = stock.getUtilisateur();
                LocalDateTime dateMouvement = stock.getDateMouvementStock().truncatedTo(ChronoUnit.SECONDS);

                HistoriqueForm aStock = new HistoriqueForm(idProduit, nomProduit, categorie, statut, dateMouvement, utilisateur);
                stockList.add(aStock);
            }
        }
        //Tri de la liste selon ID stock
        //A FAIRE
        //

        return stockList ;
    }

    //HISTORIQUE DES STOCKS SELON UNE ID PRODUIT

    public List<HistoriqueForm> historiqueStockById(long id) {
        List<StockHistorique> stocks = stockHistoriqueRepository.findAll();
        List<HistoriqueForm> stockList = new ArrayList<>();
        for (StockHistorique stock:stocks) {
            //renseignement de la liste selon l'ID produit
            if( stock.getIdProduit() == id )
            {
                long idProduit = stock.getIdProduit();
                String nomProduit = stock.getProduit();
                String categorie = stock.getCategorie();
                String statut = stock.getStatut();
                String utilisateur = stock.getUtilisateur();
                LocalDateTime dateMouvement = stock.getDateMouvementStock().truncatedTo(ChronoUnit.SECONDS);

                HistoriqueForm aStock = new HistoriqueForm(idProduit, nomProduit, categorie, statut, dateMouvement, utilisateur);
                stockList.add(aStock);
            }
        }
        //Tri de la liste selon DATE
        //A FAIRE
        //

        return stockList ;
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
        model.addAttribute("stockList", listeStock());
        return "/index";
    }

    /**
     * LISTE DES PRODUITS EN MOUVEMENT DE STOCK
     *
     * READ
     *
     *
     */

    @RequestMapping(value = { "/readStock"}, method = RequestMethod.GET)
    public String readStock(Model model) {
        model.addAttribute("stockList", listeStock());
        DateStockForm dateStock = new DateStockForm();
        model.addAttribute("dateStock",dateStock);
        return "/Stock/readStock";
    }

    /**
     * HISTORIQUE DES PRODUITS A UNE DATE DONNEE
     *
     * READ
     *
     *
     */

    @PostMapping("/readHistoriqueStock")
    public String readHistorique(@ModelAttribute("dateStock") DateStockForm dateHistorique, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return readStock(model);
        } else {
            model.addAttribute("historiqueList", historiqueStock(LocalDate.parse(dateHistorique.getDate())));
            DateStockForm dateStock = new DateStockForm();
            model.addAttribute("dateStock",dateStock);
            return "/Stock/readHistoriqueStock";
        }
    }


    /**
     * HISTORIQUE DES PRODUITS SELON ID
     *
     * READ
     *
     *
     */

    @PostMapping("/readHistoriqueStockById")
    public String readHistoriqueById(@ModelAttribute("dateStock") DateStockForm dateHistorique, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return readStock(model);
        } else {
            model.addAttribute("historiqueList", historiqueStockById(dateHistorique.getId()));
            DateStockForm dateStock = new DateStockForm();
            model.addAttribute("dateStock",dateStock);
            return "/Stock/readHistoriqueStock";
        }
    }


    /**
     * FORMULAIRE DE SAISIE ENTREE PRODUIT EN STOCK
     *
     * CREATE
     *
     */

    @RequestMapping(value = {"/createStock"}, method = RequestMethod.GET)
    public String createStock(Model model) {
        CreateStockForm aStock = new CreateStockForm();
        aStock.setListProduit(produitRepository.findAll());
        model.addAttribute("aStock", aStock);
        return "/Stock/createStock";
    }


    /**
     * SAUVEGARDE FORMULAIRE ENTREE EN STOCK
     *
     * SAVE + HISTORISATION DU MOUVEMENT
     *
     */

    @RequestMapping(value = {"/saveStock"}, method = RequestMethod.POST)
    public String saveStock(@ModelAttribute("aStock") CreateStockForm aStock, BindingResult bindingResult, Model model) throws IOException, DocumentException {
        if (bindingResult.hasErrors()) {
            createStock(model);
        } else {
            Stock stock = new Stock();
            StockHistorique historique = new StockHistorique();
            stock.setDateEntree(LocalDateTime.now());
            historique.setDateMouvementStock(LocalDateTime.now());
            Optional<Produit> produit = produitRepository.findById(aStock.getProduitId());
            produit.ifPresent(product -> {
                product.addStock(stock);
                historique.setProduit(product.getNomProduit());
                historique.setCategorie(product.getCategorie().getNomCategorie());
            });
            Statut statut = findByNomStatut("En Stock");
            statut.addStock(stock);
            historique.setStatut(statut.getNomStatut());
            historique.setUtilisateur(UtilisateurDetailsService.getNomUser());

            stockRepository.save(stock);
            historique.setIdProduit(stock.getId());
            stockHistoriqueRepository.save(historique);

            //Génération de l'étiquette PDF
            LocalDateTime dateEntree = stock.getDateEntree().truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime dlc = dateEntree.plusDays(stock.getProduit().getDureeConservation());
            dlc.truncatedTo(ChronoUnit.SECONDS);
            EtiquetteForm etiquette = new EtiquetteForm(stock.getId(), stock.getProduit().getNomProduit(), dateEntree, dlc );
            String templateHtml = parseEtiquetteTemplate(etiquette) ;
            generatePdfFromHtml (templateHtml) ;
        }
        //Retour à la liste en Stock
        return readStock(model);
    }

    /**
     * PAGE DE UPDATE STOCK VIA ID
     *
     * UPDATE
     *
     */

    @GetMapping("/updateStock/{id}")
    public String updateStockFormulaire(@PathVariable("id") long id, Model model) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid stock id:" + id));
        UpdateStockForm aStock = new UpdateStockForm(stock.getId(),stock.getProduit().getNomProduit());
        model.addAttribute("aStock", aStock);
        return "/Stock/updateStock";
    }

    @PostMapping("/updateStock")
    public String updateStock(@Valid UpdateStockForm aStock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //retour à la liste En Stock
            return readStock(model);
        }
        Stock stock = stockRepository.findById(aStock.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid stock id"));
        //Recupération du Statut entré dans le formulaire
        Statut statut = findByNomStatut(aStock.getStatut());

        //Si le statut retire le produit du stock, on le supprime du Stock
        if( statut.getNomStatut().equals("Retiré") ||
            statut.getNomStatut().equals("Erreur") ||
            statut.getNomStatut().equals("Consommé")){
            stock.getProduit().deleteStock(stock);
            stock.getStatut().deleteStock(stock);
            stockRepository.delete(stock);
        } else {   //On change le statut du stock et on sauvegarde
            stock.changeStatut(statut);
            stockRepository.save(stock);
        }

        // AJOUT D'UN NOUVEL HISTORIQUE
        StockHistorique historique = new StockHistorique();
        historique.setDateMouvementStock(LocalDateTime.now());
        historique.setProduit(stock.getProduit().getNomProduit());
        historique.setIdProduit(stock.getId());
        historique.setUtilisateur(UtilisateurDetailsService.getNomUser());
        historique.setStatut(statut.getNomStatut());
        historique.setCategorie(stock.getProduit().getCategorie().getNomCategorie());
        stockHistoriqueRepository.save(historique);

        return readStock(model);
    }

    //Méthode de parsing de l'étiquette
    private String parseEtiquetteTemplate(EtiquetteForm etiquette) throws UnsupportedEncodingException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("Etiquette", etiquette);

        String htmlContent = templateEngine.process("/templates/Stock/etiquetteStock",context);
        return convertToXhtml(htmlContent);
    }

    //Méthode de génération de l'étiquette en PDF
    public void generatePdfFromHtml(String html) throws DocumentException, IOException {
        /*File file = File.createTempFile("etiqu", ".pdf");
        OutputStream outputStream = new FileOutputStream(file) ;
*/
        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "main", "resources")
                .toUri()
                .toURL()
                .toString();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html, baseUrl);
        renderer.layout();

        OutputStream outputStream = new FileOutputStream("etiquette.pdf");
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }

}
