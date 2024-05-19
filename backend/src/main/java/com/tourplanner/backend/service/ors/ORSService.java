package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ORSService {

    private final RouteDetailsRetriever routeDetailsRetriever;
    private final GeocodeRetriever geocodeRetriever;

    public OrsParameters getOrsParameters(String fromLocation, String toLocation, String transportType) throws IOException {
        Map<String, Double> parameters = routeDetailsRetriever.parseRouteDetails(fromLocation, toLocation, transportType);

        double[] fromCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getBbox(fromLocation));
        System.out.println("fromCoordinates bbox: " + fromCoordinates[0] + ", " + fromCoordinates[1] + ", " + fromCoordinates[2] + ", " + fromCoordinates[3]);
        double[] toCoordinates = Util.stringArrayToDoubleArray(geocodeRetriever.getBbox(toLocation));
        System.out.println("toCoordinates bbox: " + toCoordinates[0] + ", " + toCoordinates[1] + ", " + toCoordinates[2] + ", " + toCoordinates[3]);

        double[] bbox = MapCreator.calculateBoundingBox(fromCoordinates, toCoordinates);
        System.out.println("final bbox: " + bbox[0] + ", " + bbox[1] + ", " + bbox[2] + ", " + bbox[3]);

        MapCreator mapCreator = new MapCreator(bbox[0], bbox[1], bbox[2], bbox[3]);
        List<double[]> wayPoints = routeDetailsRetriever.getWayPoints(fromLocation, toLocation, transportType);

        mapCreator.generateImage();
        String imageName = "address.png";
        for(double[] wayPoint : wayPoints) {
            mapCreator.getMarkers().add(new MapCreator.GeoCoordinate(wayPoint[0], wayPoint[1]));
        }
        mapCreator.saveImage(imageName);
        return new OrsParameters(parameters.get("distance"), parameters.get("duration"), imageName);
    }
}
