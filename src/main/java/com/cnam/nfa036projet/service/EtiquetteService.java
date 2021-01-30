package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.EtiquetteForm;
import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

public class EtiquetteService {


    //Méthode de parsing de l'étiquette
    public static String parseEtiquetteTemplate(EtiquetteForm etiquette) throws UnsupportedEncodingException {
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
    public static void generatePdfFromHtml(String html) throws DocumentException, IOException {
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

    private static String convertToXhtml(String html) throws UnsupportedEncodingException {
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
