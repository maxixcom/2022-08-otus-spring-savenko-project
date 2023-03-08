package io.github.maxixcom.otus.movies.backend.service.image;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageUtils {
    public static BufferedImage copyImage(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), TYPE_INT_RGB);
        Graphics graphics = dst.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, src.getWidth(), src.getHeight());
        graphics.drawImage(src, 0, 0, null);

        return dst;
    }

    private static BufferedImage scaleImage(BufferedImage src, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, TYPE_INT_RGB);

        Graphics graphics = scaledImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, newWidth, newHeight);

        Image scaledInstance = src.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH);
        graphics.drawImage(scaledInstance, 0, 0, null);

        return scaledImage;
    }

    public static BufferedImage resizeToWidth(BufferedImage src, int width) {
        int newHeight = (int) Math.ceil((double) src.getHeight() * width / src.getWidth());

        return scaleImage(src, width, newHeight);
    }

    public static BufferedImage resizeToHeight(BufferedImage src, int height) {
        int newWidth = (int) Math.ceil((double) src.getWidth() * height / src.getHeight());

        return scaleImage(src, newWidth, height);
    }

    public static BufferedImage resizeToSquare(BufferedImage src, int width, int height) {
        if (src.getWidth() > src.getHeight()) {
            return resizeToWidth(src, width);
        }
        return resizeToHeight(src, height);
    }

}
