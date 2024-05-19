package com.tourplanner.backend.service.ors;

import lombok.Data;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

@Data
public class MapCreator {

    public record GeoCoordinate(double lon, double lat) {}

    public enum Marker {
        PIN_RED_32px("marker"),
        MARKER_RED_32px( "marker-red_32px");

        Marker(String filename) {
            this.filename = filename;
        }

        public final String filename;

        public URL getResource() {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(filename + ".png");
            System.out.println("Resource URL: " + resource);
            if (resource == null) {
                System.out.println("Resource not found: " + filename);
                return null;
            }
            return resource;
        }
    }

    private final double minLon;
    private final double minLat;
    private final double maxLon;
    private final double maxLat;

    @Setter
    private int zoom = 15;
    @Setter
    private boolean cropImage = true;

    private final List<GeoCoordinate> markers = new ArrayList<>();

    private BufferedImage finalImage;

    public MapCreator(double minLon, double minLat, double maxLon, double maxLat) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
    }

    public static double[] calculateBoundingBox(double[] bbox1, double[] bbox2) {
        double minLon = Math.min(bbox1[0], bbox2[0]);
        double minLat = Math.min(bbox1[1], bbox2[1]);
        double maxLon = Math.max(bbox1[2], bbox2[2]);
        double maxLat = Math.max(bbox1[3], bbox2[3]);
        return new double[]{minLon, minLat, maxLon, maxLat};
    }

    public void generateImage() throws IOException {
        // Calculate the tile numbers for each corner of the bounding box
        var topLeftTile = TileCalculator.latlon2Tile(maxLat, minLon, zoom);
        var bottomRightTile = TileCalculator.latlon2Tile(minLat, maxLon, zoom);
        System.out.println("topLeftTile: " + topLeftTile);
        System.out.println("bottomRightTile: " + bottomRightTile);

        // Determine the number of tiles to fetch in each dimension
        int tilesX = bottomRightTile.x() - topLeftTile.x() + 1;
        int tilesY = bottomRightTile.y() - topLeftTile.y() + 1;

        System.out.println("tilesX: " + tilesX);
        System.out.println("tilesY: " + tilesY);

        // Create a new image to hold all the tiles
        if (tilesX <= 0 || tilesY <= 0) {
            throw new IllegalArgumentException("Computed tile dimensions are invalid: tilesX=" + tilesX + ", tilesY=" + tilesY);
        }
        finalImage = new BufferedImage(tilesX * 256, tilesY * 256, BufferedImage.TYPE_INT_ARGB);
        Graphics g = finalImage.getGraphics();

        // Fetch and draw each tile
        for (int x = topLeftTile.x(); x <= bottomRightTile.x(); x++) {
            for (int y = topLeftTile.y(); y <= bottomRightTile.y(); y++) {
                BufferedImage tileImage = fetchTile(zoom, x, y);
                int xPos = (x - topLeftTile.x()) * 256;
                int yPos = (y - topLeftTile.y()) * 256;
                g.drawImage(tileImage, xPos, yPos, null);
            }
        }

        PixelCalculator.Point topLeftTilePixel = new PixelCalculator.Point( topLeftTile.x() * 256, topLeftTile.y() * 256 );

        System.out.println("Number of markers to add: " + markers.size());
        // Draw Markers
        for ( var marker : markers ) {
            BufferedImage markerIcon = ImageIO.read( Marker.PIN_RED_32px.getResource() );
            if (markerIcon == null) {
                System.out.println("Marker resource is null, skipping draw.");
                continue;
            }
            PixelCalculator.Point globalPos = PixelCalculator.latLonToPixel(marker.lat(), marker.lon(), zoom);
            PixelCalculator.Point relativePos = new PixelCalculator.Point(globalPos.x() - topLeftTilePixel.x(), globalPos.y() - topLeftTilePixel.y() );
            g.drawImage(markerIcon, relativePos.x(), relativePos.y(), null);
            System.out.println("Marker added: " + marker);
        }


        // Crop the image to the exact bounding box
//        if ( cropImage ) {
//            PixelCalculator.Point bboxLeftTopGlobalPos = PixelCalculator.latLonToPixel(maxLat, minLon, zoom);
//            PixelCalculator.Point bboxRightBottomGlobalPos = PixelCalculator.latLonToPixel(minLat, maxLon, zoom);
//            PixelCalculator.Point bboxLeftTopRelativePos = new PixelCalculator.Point(bboxLeftTopGlobalPos.x() - topLeftTilePixel.x(), bboxLeftTopGlobalPos.y() - topLeftTilePixel.y());
//            int width = bboxRightBottomGlobalPos.x() - bboxLeftTopGlobalPos.x();
//            int height = bboxRightBottomGlobalPos.y() - bboxLeftTopGlobalPos.y();
//            finalImage = finalImage.getSubimage(bboxLeftTopRelativePos.x(), bboxLeftTopRelativePos.y(), width, height);
//        }

        g.dispose();
    }

    public void saveImage(String filename) throws IOException {
        ImageIO.write(finalImage, "png", new File(filename));
    }

    private static BufferedImage fetchTile(int zoom, int x, int y) throws IOException {
        String tileUrl = "https://tile.openstreetmap.org/" + zoom + "/" + x + "/" + y + ".png";
        URL url = new URL(tileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("User-Agent", "TourPlanner/1.0 (georgezudikhin@gmail.com)");

        try (InputStream inputStream = httpConn.getInputStream()) {
            return ImageIO.read(inputStream);
        } finally {
            httpConn.disconnect();
        }
    }
}
