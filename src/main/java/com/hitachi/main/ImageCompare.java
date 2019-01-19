package com.hitachi.main;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hitachi.utils.ImageUtils;

public class ImageCompare {
    public static void main(String[] args) {
        try {
            File file1 = new File("D:\\test\\1.png");
            File file2 = new File("D:\\test\\2.png");
            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage image2 = ImageIO.read(file2);
            int end1X = 0;
            int end1Y = 0;
            int end2X = 0;
            int end2Y = 0;
            for (int i = 1; i < image1.getHeight(); i++) {
                if (image1.getRGB(0, i) != image1.getRGB(0, 0)) {
                    end1Y = i;
                    break;
                }
            }
            for (int i = 1; i < image1.getHeight(); i++) {
                if (image1.getRGB(i, end1Y) != image1.getRGB(0, end1Y)) {
                    end1X = i;
                    break;
                }
            }
            for (int i = 1; i < image2.getHeight(); i++) {
                if (image2.getRGB(0, i) != image2.getRGB(0, 0)) {
                    end2Y = i;
                    break;
                }
            }
            for (int i = 1; i < image2.getHeight(); i++) {
                if (image2.getRGB(i, end2Y) != image2.getRGB(0, end2Y)) {
                    end2X = i;
                    break;
                }
            }
            image1 = ImageUtils.zoomByScale(image1, end2X / end1X, end2Y / end1Y);
            BufferedImage image3 = new BufferedImage(image1.getWidth(), image1.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < image1.getWidth(); i++) {
                if (image2.getWidth() <= i) {
                    break;
                }
                for (int j = 0; j < image1.getHeight(); j++) {
                    if (image2.getHeight() <= j) {
                        break;
                    }
                    Object pixel = null;
                    image1.getRaster().getDataElements(i, j, pixel);
                    if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
                        WritableRaster raster = image3.getAlphaRaster();
                        //                        raster.setDataElements(x, y, inData);
                    }
                }
            }

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
}
