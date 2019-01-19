package com.hitachi.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {
    /**
    * 合并任数量的图片成一张图片
    *
    * @param isHorizontal
    *            true代表水平合并，fasle代表垂直合并
    * @param imgs
    *            待合并的图片数组
    * @return
    * @throws IOException
    */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs) throws IOException {
        // 生成新图片
        BufferedImage destImage = null;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            allw += img.getWidth();
            allh += img.getHeight();
            if (img.getWidth() > allwMax) {
                allwMax = img.getWidth();
            }
            if (img.getHeight() > allhMax) {
                allhMax = img.getHeight();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage(allw, allhMax, BufferedImage.TYPE_INT_RGB);
        } else {
            destImage = new BufferedImage(allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth();
            int h1 = img.getHeight();
            // 从图片中读取RGB
            int[] ImageArrayOne = new int[w1 * h1];
            ImageArrayOne = img.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
            if (isHorizontal) { // 水平方向合并
                destImage.setRGB(wx, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            } else { // 垂直方向合并
                destImage.setRGB(0, wy, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }

    public static BufferedImage cutImage(int fromX, int toX, int fromY, int toY, BufferedImage image) {
        if(fromY<0)
            fromY = 0;
        BufferedImage destImage = new BufferedImage(toX - fromX, toY - fromY, BufferedImage.TYPE_INT_RGB);
        int w1 = destImage.getWidth();
        int h1 = destImage.getHeight();
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = image.getRGB(fromX, fromY, w1, h1, ImageArrayOne, 0, w1);
        destImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
        return destImage;
    }

    public static BufferedImage zoomByScale(BufferedImage img, double scale) throws IOException {
        int _width = (int) (scale * img.getWidth());
        int _height = (int) (scale * img.getHeight());
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(_width, _height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }
    public static BufferedImage zoomByScale(BufferedImage img, double scaleW,double scaleH) throws IOException {
        int _width = (int) (scaleW * img.getWidth());
        int _height = (int) (scaleH * img.getHeight());
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(_width, _height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }

    public static BufferedImage zoomByScale(BufferedImage img) throws IOException {
        double scale = 0.8;
        int _width = (int) (scale * img.getWidth());
        int _height = (int) (scale * img.getHeight());
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(_width, _height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }
}
