package com.tourplanner.backend.service.ors;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeocodeRetriever {

    @Value("${api_key}")
    private String api_key;

    private final RestTemplate restTemplate;

    public String[] getCoordinates(String address) {
        String geoDataResponse = getORSData(address);
        return parseCoordinates(geoDataResponse);
    }

    public String[] getBbox(String address) {
        String geoDataResponse = getORSData(address);
        return parseBbox(geoDataResponse);
    }

    private String[] parseCoordinates(String geoDataResponse) {
        JSONObject geoData = new JSONObject(geoDataResponse);
        JSONArray coordinates = geoData.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        String[] coordinatesArray = new String[2];
        coordinatesArray[0] = Double.toString(coordinates.getDouble(0));
        coordinatesArray[1] = Double.toString(coordinates.getDouble(1));
        return coordinatesArray;
    }

    private String[] parseBbox(String geoDataResponse) {
        JSONObject geoData = new JSONObject(geoDataResponse);
        JSONArray coordinates = geoData.getJSONArray("bbox");
        String[] bboxArray = new String[4];
        bboxArray[0] = Double.toString(coordinates.getDouble(0));
        bboxArray[1] = Double.toString(coordinates.getDouble(1));
        bboxArray[2] = Double.toString(coordinates.getDouble(2));
        bboxArray[3] = Double.toString(coordinates.getDouble(3));
        return bboxArray;
    }

    private String getORSData(String address) {
        return restTemplate.getForEntity(
                "https://api.openrouteservice.org/geocode/search?api_key=" + api_key + "&text=" + address,
                String.class).getBody();
    }
}
