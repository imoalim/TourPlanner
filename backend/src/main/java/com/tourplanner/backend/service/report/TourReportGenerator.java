package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.util.Util;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import com.tourplanner.backend.service.s3.S3FileUploadService;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.List;

@Setter
@Component
public class TourReportGenerator extends ReportGenerator {

    private Long tourID;

    public TourReportGenerator(S3FileUploadService s3FileUploadService, TourServiceImpl tourService) {
        super(s3FileUploadService, tourService);
    }

    @Override
    public String generateReport() {
        String fileName = generateUniqueFileName();
        String htmlContent = parseThymeleafTemplate("thymeleaf/tour_report.html");
        return generatePdf(htmlContent, fileName);
    }

    @Override
    protected String generateUniqueFileName() {
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date());
        return "TourReport_" + tourID + "_" + timeStamp + ".pdf";
    }

    @Override
    protected Context setContext() {
        Context context = new Context();
        context.setVariable("tour", getTourData());
        context.setVariable("tour_logs", getTourLogData());

        return context;
    }

    private TourDTO getTourData() {
        TourDTO tour = tourService.findById(tourID);
        tour.setDistance(Util.roundToTwoDecimalPlaces(tour.getDistance() / 1000));
        tour.setEstimatedTime(Util.roundToNearestInt(tour.getEstimatedTime() / 60));

        return tour;
    }

    private List<TourLogDTO> getTourLogData() {
        List<TourLogDTO> tourLogs = tourService.getAllTourLogsForThisTour(tourID);
        tourLogs.forEach(tourLog -> tourLog.setDistance(Util.roundToTwoDecimalPlaces(tourLog.getDistance() / 1000)));
        tourLogs.forEach(tourLog -> tourLog.setTotalTime(Util.roundToNearestInt(tourLog.getTotalTime() / 60)));

        return tourLogs;
    }
}
