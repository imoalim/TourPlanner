package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import com.tourplanner.backend.service.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ORSService {

    private final RouteDetailsRetriever routeDetailsRetriever;

    public OrsParameters getOrsParameters(String fromLocation, String toLocation, String transportType) {
        routeDetailsRetriever.setProperties(fromLocation, toLocation, transportType);
        routeDetailsRetriever.getOrsRouteDetails();
        Map<String, Double> parameters = routeDetailsRetriever.parseRouteDetails();
        List<double[]> route = routeDetailsRetriever.getWayPoints();

        double[] center = calculateMapCenter(route);

        MapInfoDTO mapInfoDTO = MapInfoDTO.builder()
                .startLat(route.get(0)[0])
                .startLng(route.get(0)[1])
                .endLat(route.get(route.size() - 1)[0])
                .endLng(route.get(route.size() - 1)[1])
                .centerLat(center[0])
                .centerLng(center[1])
                .route(Util.formatRouteForJs(route))
                .build();

        return new OrsParameters(parameters.get("distance"), parameters.get("duration"), mapInfoDTO);
    }

    private double[] calculateMapCenter(List<double[]> routeCoordinates) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLng = Double.MIN_VALUE;

        for (double[] coords : routeCoordinates) {
            double lat = coords[0];
            double lng = coords[1];

            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lng < minLng) minLng = lng;
            if (lng > maxLng) maxLng = lng;
        }

        double centerLat = (minLat + maxLat) / 2;
        double centerLng = (minLng + maxLng) / 2;

        return new double[]{centerLat, centerLng};
    }
}
