//package com.tourplanner.backend.map;
//
//import lombok.RequiredArgsConstructor;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class MapApi {
//
//    @Value("${api_key}")
//    private String api_key;
//
//    private final RestTemplate restTemplate;
//
//    public String searchAddress(String text) {
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "https://api.openrouteservice.org/geocode/search?boundary.country=AT&api_key=" + api_key + "&text=" + text,
//                String.class);
//
//        JSONObject jsonResponse = new JSONObject(response.getBody());
//        JSONArray features = jsonResponse.getJSONArray("features");
//        JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
//        JSONArray coordinates = geometry.getJSONArray("coordinates");
//
//        return coordinates.getDouble(0) + "," + coordinates.getDouble(1);
//    }
//
//    public List<double[]> searchDirection(String start, String end) {
//        String[] profiles = {"driving-car", "cycling-regular", "foot-walking"};
//        String profile = profiles[0];  // Default to driving-car
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "https://api.openrouteservice.org/v2/directions/" + profile + "?api_key=" + api_key + "&start=" + start + "&end=" + end,
//                String.class);
//
//        List<double[]> routeCoordinates = new ArrayList<>();
//        if (response.getStatusCode() == HttpStatus.OK) {
//            JSONObject jsonResponse = new JSONObject(response.getBody());
//            JSONArray features = jsonResponse.getJSONArray("features");
//            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
//            JSONArray coordinates = geometry.getJSONArray("coordinates");
//
//            for (int i = 0; i < coordinates.length(); i++) {
//                JSONArray coord = coordinates.getJSONArray(i);
//                double longitude = coord.getDouble(0);
//                double latitude = coord.getDouble(1);
//                routeCoordinates.add(new double[]{latitude, longitude});
//            }
//        }
//        return routeCoordinates;
//    }
//
//    public String getMapUrl(String start, String end) {
//        return "https://www.openstreetmap.org/directions?engine=fossgis_osrm_car&route=" + start + ";" + end;
//    }
//}
