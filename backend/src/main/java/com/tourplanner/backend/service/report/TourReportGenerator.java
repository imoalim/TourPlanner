package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import com.tourplanner.backend.service.s3.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TourReportGenerator{

    private final TourServiceImpl tourService;

    private final S3FileUploadService s3FileUploadService;

    protected String generateReport(Long tourId) {
        String fileName = generateUniqueFileName(tourId);
        String htmlContent = parseThymeleafTemplateTour(tourId);
        return generatePdf(htmlContent, fileName);
    }

    private String generateUniqueFileName(Long tourId) {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "TourReport_" + tourId + "_" + timeStamp + ".pdf";
    }

    private String parseThymeleafTemplateTour(Long tourId) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("tour", getTourData(tourId));
        context.setVariable("tour_logs", getTourLogData(tourId));

        return templateEngine.process("thymeleaf/tour_report", context);
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

    private TourResponseDTO getTourData(Long tourId) {
        return tourService.findById(tourId);
    }

    private List<TourLogDTO> getTourLogData(Long tourId) {
        return tourService.getAllTourLogsForThisTour(tourId);
    }
}
