package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
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

    private TourResponseDTO getTourData() {
        return tourService.findById(tourID);
    }

    private List<TourLogDTO> getTourLogData() {
        return tourService.getAllTourLogsForThisTour(tourID);
    }
}
