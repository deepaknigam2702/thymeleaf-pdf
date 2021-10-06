package com.baeldung.pdfthymeleaf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class PDFThymeleafExample {

    public static void main(String[] args) throws IOException, DocumentException {
        PDFThymeleafExample thymeleaf2Pdf = new PDFThymeleafExample();
        String html = thymeleaf2Pdf.parseThymeleafTemplate();
        thymeleaf2Pdf.generatePdfFromHtml(html);
    }

    public void generatePdfFromHtml(String html) throws IOException, DocumentException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private String parseThymeleafTemplate() throws IOException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "Baeldung.coms");
        context.setVariable("image",  "Java_logo.png");
        
        
        DefaultPieDataset defaultCategoryDataset = new DefaultPieDataset();
        defaultCategoryDataset.setValue("Linux", 10);
        defaultCategoryDataset.setValue("Mac OS", 13);
        defaultCategoryDataset.setValue("Windows", 75);
        defaultCategoryDataset.setValue("Unix", 2);

        JFreeChart jFreeChart = ChartFactory.createPieChart(
                     "Operating System users",  //pie chart title
                     defaultCategoryDataset, //pie chart dataset
                     false, false, false);  //pie chart> legend, tooltips and urls
        
        OutputStream out = new FileOutputStream("chart.png");
        
        ChartUtilities.writeChartAsPNG(out,
                jFreeChart,
                500,
                500);
        
        context.setVariable("chart",  "chart.png");

        return templateEngine.process("thymeleaf_template", context);
    }
}
