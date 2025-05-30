/* Said Zitouni (C)2025 */
package com.saidworks.practice.performance.latency;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RgbUtil {
    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage outputImage, int numThreads)
            throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int threadMultiplier = i;
            threads.add(new Thread((Runnable) () -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                reColorImageSingleThreaded(originalImage, outputImage, leftCorner, topCorner, width, height);
            }));
        }
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
    }

    public static void reColorImageSingleThreaded(
            BufferedImage originalImage, BufferedImage newImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, newImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage newImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed = red;
        int newGreen = green;
        int newBlue = blue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        }
        int newRgb = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(newImage, x, y, newRgb);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
