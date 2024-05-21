package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.dto.report.SummaryDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import com.tourplanner.backend.service.s3.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SummarizeReportGenerator {

    private final TourServiceImpl tourService;

    private final S3FileUploadService s3FileUploadService;

    public String generateReport() {
        String fileName = generateUniqueFileName();
        String htmlContent = parseThymeleafTemplateSummary();
        return generatePdf(htmlContent, fileName);
    }

    private String generateUniqueFileName() {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "SummarizeReport_" + "_" + timeStamp + ".pdf";
    }

    private String parseThymeleafTemplateSummary() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        List<TourResponseDTO> allTours = getAllTours();
        context.setVariable("tours", allTours);
        context.setVariable("summaries", allTours.stream().map(this::calculateAverages).collect(Collectors.toList()));

        return templateEngine.process("thymeleaf/summarize_report", context);
    }

    protected String generatePdf(String html, String fileName) {
        Path outputPath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        try (OutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }

        try {
            String fileUrl = s3FileUploadService.uploadFileToS3(outputPath, fileName);
            Files.deleteIfExists(outputPath);  // Clean up the temporary file
            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload PDF to S3", e);
        }
    }

    private List<TourResponseDTO> getAllTours() {
        return tourService.findAll();
    }

    private SummaryDTO calculateAverages(TourResponseDTO tour) {
        List<TourLogDTO> logs = tourService.getAllTourLogsForThisTour(tour.getId());
        double averageDistance = logs.stream().mapToDouble(TourLogDTO::getDistance).average().orElse(0) / 1000;
        double averageTime = logs.stream().mapToDouble(TourLogDTO::getTotalTime).average().orElse(0) / 3600;
        double averageRating = logs.stream().mapToDouble(TourLogDTO::getRating).average().orElse(0);

        return new SummaryDTO(tour.getName(), averageDistance, averageTime, averageRating);
    }
}
