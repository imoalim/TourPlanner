package com.tourplanner.backend.service.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

public abstract class AbstractReportGenerator {

    protected abstract Context buildContext();

    protected abstract String getTemplateName();

    protected String generatePdf(String html) throws Exception {
        String output = this.getClass().getSimpleName().toLowerCase().replace("generator", "_report.pdf");
        try (OutputStream outputStream = new FileOutputStream(output)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
        }
        return output;
    }

    protected String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = buildContext();
        return templateEngine.process(getTemplateName(), context);
    }

    public String generateReport() throws Exception {
        return generatePdf(parseThymeleafTemplate());
    }
}
