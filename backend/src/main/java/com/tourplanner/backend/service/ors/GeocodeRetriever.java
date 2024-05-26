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

    public String getCoordinates(String address) {
        String geoDataResponse = getORSData(address);
        return parseCoordinates(geoDataResponse);
    }

    private String parseCoordinates(String geoDataResponse) {
        JSONObject geoData = new JSONObject(geoDataResponse);
        JSONArray coordinates = geoData.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

        return coordinates.getDouble(0) + "," + coordinates.getDouble(1);
    }

    private String getORSData(String address) {
        return restTemplate.getForEntity(
                "https://api.openrouteservice.org/geocode/search?api_key=" + api_key + "&text=" + address,
                String.class).getBody();
    }
}
