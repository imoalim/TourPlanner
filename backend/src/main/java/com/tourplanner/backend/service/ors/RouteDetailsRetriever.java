package com.tourplanner.backend.service.ors;

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

    private String fromLocation;
    private String toLocation;
    private String transportType;
    private JSONObject orsRouteData;

    public void setProperties(String fromLocation, String toLocation, String transportType) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.transportType = transportType;
    }

    public List<double[]> getWayPoints() {
        List<double[]> wayPoints = new ArrayList<>();

        JSONArray routeCoordinates = orsRouteData.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

        // Swapping longitude and latitude for each coordinate
        for (int i = 0; i < routeCoordinates.length(); i++) {
            JSONArray coord = routeCoordinates.getJSONArray(i);
            double longitude = coord.getDouble(0);
            double latitude = coord.getDouble(1);
            wayPoints.add(new double[]{latitude, longitude});
        }
        return wayPoints;
    }

    public Map<String, Double> parseRouteDetails() {
        JSONObject summary = orsRouteData.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getJSONObject("summary");
        double distance = summary.getDouble("distance");
        double duration = summary.getDouble("duration");
        return Map.of("distance", distance, "duration", duration);
    }

    public void getOrsRouteDetails() {
        String startCoordinates = geocodeRetriever.getCoordinates(fromLocation);
        String endCoordinates = geocodeRetriever.getCoordinates(toLocation);

        orsRouteData = new JSONObject(restTemplate.getForEntity(
                "https://api.openrouteservice.org/v2/directions/" + transportType + "?api_key=" + api_key +
                        "&start=" + startCoordinates +
                        "&end=" + endCoordinates,
                String.class).getBody());
    }
}
