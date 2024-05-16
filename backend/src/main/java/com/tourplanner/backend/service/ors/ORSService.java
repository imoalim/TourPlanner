package com.tourplanner.backend.service.ors;

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

    public OrsParameters getOrsParameters(String fromLocation, String toLocation, String transportType) throws IOException {
        Map<String, Double> parameters = routeDetailsRetriever.getRouteDetails(fromLocation, toLocation, transportType);

        double[] fromCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getCoordinates(fromLocation));
        double[] toCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getCoordinates(toLocation));
        double[] bbox = MapCreator.calculateBoundingBox(fromCoordinates, toCoordinates);

        MapCreator mapCreator = new MapCreator(bbox[0], bbox[1], bbox[2], bbox[3]);
        mapCreator.setZoom(18);

        mapCreator.generateImage();
        String imageName = "FHTW-map.png";
        mapCreator.saveImage(imageName);
        return new OrsParameters(parameters.get("distance"), parameters.get("duration"), imageName);
    }
}
