package com.hitachi.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AddImageToExcel {
    public static void main(String[] args) {
        File dir = new File("D:\\evidence_layout");
        for (File hsFile : dir.listFiles()) {
            if (hsFile.isDirectory()) {
                try {

                    File file = new File(hsFile.getAbsoluteFile() + "\\" + hsFile.getName() + ".xls");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    for (File pic : hsFile.listFiles()) {
                        if (pic.getName().startsWith("ie_")) {
                            HSSFSheet sheet = workbook.createSheet(pic.getName()
                                    .substring(pic.getName().indexOf("_") + 1, pic.getName().indexOf(".")));
                            addPicLayout(workbook, sheet, pic);
                        }
                        FileOutputStream output = new FileOutputStream(file);
                        workbook.write(output);
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

    private static void addPic(HSSFWorkbook workbook, HSSFSheet sheet, File pic) throws IOException {
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
        cell.setCellValue("ie");
        cell = row.createCell(image.getWidth() / 64 + 1);
        cell.setCellValue("edge");

        image = ImageIO.read(new File(pic.getAbsolutePath().replace("ie_", "edge_")));
        ImageIO.write(image, "png", byteArrayOut);
        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (image.getWidth() / 64 + 1), 1,
                (short) (image.getWidth() / 64 * 2 + 1),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
    }

}
