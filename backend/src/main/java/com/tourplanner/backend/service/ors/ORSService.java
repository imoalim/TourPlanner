package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.s3.S3FileUploadService;
import com.tourplanner.backend.service.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ORSService {

    private final RouteDetailsRetriever routeDetailsRetriever;
    private final GeocodeRetriever geocodeRetriever;
    private final S3FileUploadService s3FileUploadService;

    public OrsParameters getOrsParameters(String fromLocation, String toLocation, String transportType) throws IOException {
        Map<String, Double> parameters = routeDetailsRetriever.parseRouteDetails(fromLocation, toLocation, transportType);
        String imageURL = getImageURL(fromLocation, toLocation);

        return new OrsParameters(parameters.get("distance"), parameters.get("duration"), imageURL);
    }

    private String getImageURL(String fromLocation, String toLocation) throws IOException {
        double[] fromCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getBbox(fromLocation));
        double[] toCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getBbox(toLocation));

        double[] bbox = MapCreator.calculateBoundingBox(fromCoordinates, toCoordinates);

        MapCreator mapCreator = new MapCreator(bbox[0], bbox[1], bbox[2], bbox[3], s3FileUploadService);

        mapCreator.generateImage();
        return mapCreator.saveImageToS3();
    }
}
