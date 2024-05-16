package com.tourplanner.backend.service.ors;

public class TileCalculator {
    public record Tile(int x, int y) {}

    public static Tile latlon2Tile(double lat_deg, double lon_deg, int zoom) {
        double lat_rad = Math.toRadians(lat_deg);
        double n = Math.pow(2.0, zoom);
        int x_tile = (int) Math.floor((lon_deg + 180.0) / 360.0 * n);
        int y_tile = (int) Math.floor((1.0 - Math.log(Math.tan(lat_rad) + 1 / Math.cos(lat_rad)) / Math.PI) / 2.0 * n);
        return new Tile(x_tile, y_tile);
    }
}
