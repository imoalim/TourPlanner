package com.tourplanner.backend.service.ors;

public class PixelCalculator {
    public record Point(int x, int y) {}

    public static Point latLonToPixel(double lat, double lon, int zoom) {
        double lat_rad = Math.toRadians(lat);
        double n = Math.pow(2.0, zoom);
        int x_pixel = (int) Math.floor((lon + 180.0) / 360.0 * n * 256);
        int y_pixel = (int) Math.floor((1.0 - Math.log(Math.tan(lat_rad) + 1 / Math.cos(lat_rad)) / Math.PI) / 2.0 * n * 256);

        return new Point(x_pixel, y_pixel);
    }
}
