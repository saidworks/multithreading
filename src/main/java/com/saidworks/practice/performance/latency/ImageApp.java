/* Said Zitouni (C)2025 */
package com.saidworks.practice.performance.latency;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageApp {
    public static final String SOURCE_FILE = "./src/main/resources/latency/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./src/main/resources/latency/many-flowers-out.jpg";
    private static final Logger log = LogManager.getLogger(ImageApp.class.getName());

    public static void main(String[] args) {
        BufferedImage inputImage = null;
        BufferedImage outputImage = null;
        try {
            inputImage = ImageIO.read(new File(SOURCE_FILE));
            outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();

            long startTime = System.currentTimeMillis();
            // 311ms one thread
            //            RgbUtil.reColorImageSingleThreaded(inputImage, outputImage,0,0, width, height);
            // 291ms with 4 threads and 270ms with 6
            RgbUtil.recolorMultiThreaded(inputImage, outputImage, 6);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("Elapsed time: " + elapsedTime + " ms");
            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(outputImage, "jpg", outputFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
