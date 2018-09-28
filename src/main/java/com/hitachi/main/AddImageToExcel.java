package com.hitachi.main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AddImageToExcel {
    public static void main(String[] args) {
        File dir = new File("D:\\aaaa\\aaaa");
        for (File hsFile : dir.listFiles()) {
            if (hsFile.isDirectory()) {
                try {
                    File file = new File(hsFile.getAbsoluteFile() + "\\" + hsFile.getName() + ".xls");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    System.out.println(file.getName());
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    File[] pics = hsFile.listFiles();
                    for (File pic : pics) {
                        if (pic.getName().startsWith("ie_")) {
                            System.out.println(pic.getName()
                                    .substring(pic.getName().indexOf("_") + 1, pic.getName().indexOf(".")));
                            HSSFSheet sheet = workbook.createSheet(pic.getName()
                                    .substring(pic.getName().indexOf("_") + 1, pic.getName().indexOf(".")));
                            addPic(workbook, sheet, pic);
                        }
                        FileOutputStream output = new FileOutputStream(file);
                        workbook.write(output);
                        output.close();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static void addPicLayout(HSSFWorkbook workbook, HSSFSheet sheet, File pic) throws IOException {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFRow row = sheet.createRow(0);
        BufferedImage image = ImageIO.read(pic);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOut);
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 1, (short) (image.getWidth() / 64),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Layout");
    }

    private static void addPicPankuzu(HSSFWorkbook workbook, HSSFSheet sheet, File pic) throws IOException {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFRow row = sheet.createRow(0);
        BufferedImage image = ImageIO.read(pic);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOut);
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 1, (short) (image.getWidth() / 64),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Pankuzu");
    }

    private static void addPic(HSSFWorkbook workbook, HSSFSheet sheet, File pic) throws IOException {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ie");

        BufferedImage image = ImageIO.read(pic);
        image = zoomByScale(image);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOut);
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 1, (short) (image.getWidth() / 64),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        int col = image.getWidth() / 64 + 1;

        cell = row.createCell(col);
        cell.setCellValue("edge");

        image = ImageIO.read(new File(pic.getAbsolutePath().replace("ie_", "edge_")));
        image = zoomByScale(image,1.0);

        ImageIO.write(image, "png", byteArrayOut);
        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (col), 1,
                (short) (image.getWidth() / 64 +col),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

        col = col + image.getWidth() / 64 + 1;

        cell = row.createCell(col);
        cell.setCellValue("ct8");

        image = ImageIO.read(new File(pic.getAbsolutePath().replace("ie_", "CT8_ie_")));

        image = zoomByScale(image);

        ImageIO.write(image, "png", byteArrayOut);
        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (col), 1,
                (short) (image.getWidth() / 64 + col),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG))	;

    }

    public static BufferedImage zoomByScale(BufferedImage img) throws IOException {
        double scale = 0.7;
        int _width = (int) (scale * img.getWidth()*0.7) ;
        int _height = (int) (scale * img.getHeight());
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(_width, _height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }
    public static BufferedImage zoomByScale(BufferedImage img,double scale) throws IOException {
        int _width = (int) (scale * img.getWidth() *0.7);
        int _height = (int) (scale * img.getHeight());
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(_width, _height, Image.SCALE_SMOOTH), 0, 0, null);
        return image;
    }

}
