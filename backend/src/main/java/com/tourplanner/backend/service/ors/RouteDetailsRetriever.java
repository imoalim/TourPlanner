package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.util.Util;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteDetailsRetriever {

    @Value("${api_key}")
    private String api_key;

    private final GeocodeRetriever geocodeRetriever;

    private final RestTemplate restTemplate;

    public List<double[]> getWayPoints(String fromLocation, String toLocation, String transportType) {
        String orsRouteResponse = getOrsRouteDetails(fromLocation, toLocation, transportType);
        List<double[]> wayPoints = new ArrayList<>();

        JSONObject orsRoute = new JSONObject(orsRouteResponse);
        JSONArray routeCoordinates = orsRoute.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

        // Swapping longitude and latitude for each coordinate
        for (int i = 0; i < routeCoordinates.length(); i++) {
            JSONArray coord = routeCoordinates.getJSONArray(i);
            double longitude = coord.getDouble(0);
            double latitude = coord.getDouble(1);
            wayPoints.add(new double[]{longitude, latitude});
        }
        return wayPoints;
    }

    public Map<String, Double> parseRouteDetails(String fromLocation, String toLocation, String transportType) {
        String orsRouteResponse = getOrsRouteDetails(fromLocation, toLocation, transportType);
        JSONObject orsRouteData = new JSONObject(orsRouteResponse);
        JSONObject summary = orsRouteData.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getJSONObject("summary");
        double distance = summary.getDouble("distance");
        double duration = summary.getDouble("duration");
        return Map.of("distance", distance, "duration", duration);
    }

    private String getOrsRouteDetails(String fromLocation, String toLocation, String transportType) {
        String[] startCoordinates = geocodeRetriever.getCoordinates(fromLocation);
        String[] endCoordinates = geocodeRetriever.getCoordinates(toLocation);

        String start = Util.joinStrings(startCoordinates);
        String end = Util.joinStrings(endCoordinates);
        return restTemplate.getForEntity(
                "https://api.openrouteservice.org/v2/directions/" + transportType + "?api_key=" + api_key +
                        "&start=" + start +
                        "&end=" + end,
                String.class).getBody();
    }
}
