package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.s3.S3FileUploadService;
import lombok.Data;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Data
public class MapCreator {

    private final double minLon;
    private final double minLat;
    private final double maxLon;
    private final double maxLat;

    private final S3FileUploadService s3FileUploadService;

    @Setter
    private int zoom = 15;
    @Setter
    private boolean cropImage = true;

    private BufferedImage finalImage;

    public MapCreator(double minLon, double minLat, double maxLon, double maxLat, S3FileUploadService s3FileUploadService) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
        this.s3FileUploadService = s3FileUploadService;
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

        // Determine the number of tiles to fetch in each dimension
        int tilesX = bottomRightTile.x() - topLeftTile.x() + 1;
        int tilesY = bottomRightTile.y() - topLeftTile.y() + 1;

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

        g.dispose();
    }

    public String saveImageToS3() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(finalImage, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);

        String filename = generateUniqueImageName();

        return s3FileUploadService.uploadFileToS3(bis, filename, imageInByte.length);
    }

    private String generateUniqueImageName() {
        String dateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "maps/image_" + dateTime + ".png";
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
