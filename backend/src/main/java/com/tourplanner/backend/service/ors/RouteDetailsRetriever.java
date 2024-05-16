package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.util.Util;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteDetailsRetriever {

    @Value("${api_key}")
    private String api_key;

    private final GeocodeRetriever geocodeRetriever;

    private final RestTemplate restTemplate;

    public Map<String, Double> getRouteDetails(String fromLocation, String toLocation, String transportType) {
        String orsRouteResponse = getOrsRouteDetails(fromLocation, toLocation, transportType);
        JSONObject orsRouteData = new JSONObject(orsRouteResponse);
        JSONObject summary = orsRouteData.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getJSONObject("summary");
        double distance = summary.getDouble("distance");
        double duration = summary.getDouble("duration");
        return Map.of("distance", distance, "duration", duration);
    }

    public String getOrsRouteDetails(String fromLocation, String toLocation, String transportType) {
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
