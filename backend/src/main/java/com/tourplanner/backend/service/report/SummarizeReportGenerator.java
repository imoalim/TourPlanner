package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.dto.report.SummaryDTO;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import com.tourplanner.backend.service.s3.S3FileUploadService;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SummarizeReportGenerator extends ReportGenerator {

    public SummarizeReportGenerator(S3FileUploadService s3FileUploadService, TourServiceImpl tourService) {
        super(s3FileUploadService, tourService);
    }

    @Override
    public String generateReport() {
        String fileName = generateUniqueFileName();
        String htmlContent = parseThymeleafTemplate("thymeleaf/summarize_report.html");
        return generatePdf(htmlContent, fileName);
    }

    @Override
    protected String generateUniqueFileName() {
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date());
        return "SummarizeReport_" + timeStamp + ".pdf";
    }

    @Override
    protected Context setContext() {
        Context context = new Context();
        List<TourResponseDTO> allTours = getAllTours();
        context.setVariable("tours", allTours);
        context.setVariable("summaries", allTours.stream().map(this::calculateAverages).collect(Collectors.toList()));

        return context;
    }

    private List<TourResponseDTO> getAllTours() {
        return tourService.findAll();
    }

    private SummaryDTO calculateAverages(TourResponseDTO tour) {
        List<TourLogDTO> tourLogs = tourService.getAllTourLogsForThisTour(tour.getId());

        double averageDistance = roundToTwoDecimalPlaces(tourLogs.stream()
                .mapToDouble(TourLogDTO::getDistance)
                .average()
                .orElse(0) / 1000);

        double averageTime = roundToTwoDecimalPlaces(tourLogs.stream()
                .mapToDouble(TourLogDTO::getTotalTime)
                .average()
                .orElse(0) / 3600);

        double averageRating = roundToTwoDecimalPlaces(tourLogs.stream()
                .mapToDouble(TourLogDTO::getRating)
                .average()
                .orElse(0));

        return new SummaryDTO(tour.getName(), averageDistance, averageTime, averageRating);
    }

    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
