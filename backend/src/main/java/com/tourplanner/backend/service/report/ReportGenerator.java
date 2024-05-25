package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.impl.TourServiceImpl;
import com.tourplanner.backend.service.s3.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;

@RequiredArgsConstructor
public abstract class ReportGenerator {

    protected final S3FileUploadService s3FileUploadService;

    protected final TourServiceImpl tourService;

    public abstract String generateReport();
    protected abstract String generateUniqueFileName();
    protected abstract Context setContext();

    protected String parseThymeleafTemplate(String templateName) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(templateName, setContext());
    }

    protected String generatePdf(String htmlContent, String fileName) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream);

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);

            return s3FileUploadService.uploadFileToS3(byteArrayInputStream, fileName, pdfBytes.length);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate and upload PDF", e);
        }
    }
}
