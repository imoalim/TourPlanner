package com.tourplanner.backend.service.map;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GeocodeRetriever {

    @Value("${api_key}")
    private String api_key;

    private final WebClient webClient;

    public Mono<String[]> getCoordinates(String address) {
        return getORSData(address).map(geoDataResponse -> {
            JSONObject geoData = new JSONObject(geoDataResponse);
            JSONArray coordinates = geoData.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
            String[] coordinatesArray = new String[2];
            coordinatesArray[0] = Double.toString(coordinates.getDouble(0));
            coordinatesArray[1] = Double.toString(coordinates.getDouble(1));
            return coordinatesArray;
        });
    }

    public Mono<String> getORSData(String address) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                                .host("api.openrouteservice.org")
                                .path("/geocode/search")
                                .queryParam("api_key", api_key)
                                .queryParam("text", address)
                                .build())
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(String.class);
    }

    private Mono<? extends Throwable> handleErrorResponse(HttpStatusCode httpStatusCode) {
        return Mono.error(new RuntimeException("Failed to fetch geodata: " + httpStatusCode));
    }
}
