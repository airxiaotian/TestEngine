package com.hitachi.main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hitachi.utils.ImageUtils;

public class AddImageToExcel {
    public static void main(String[] args) {
        File dir = new File("D:\\test\\client1");
        for (File hsFile : dir.listFiles()) {
            try {
                if (hsFile.isDirectory()) {

                    Map<String, String> map = new HashMap<String, String>();
                    File hssFile = new File("D:\\JCIS分\\完了\\" + hsFile.getName() + "_2_画面項目定義書.xls");
                    HSSFWorkbook workbook2 = new HSSFWorkbook(new FileInputStream(hssFile));
                    HSSFSheet sheet2 = workbook2.getSheetAt(0);
                    int index = 3;
                    HSSFRow row = sheet2.getRow(index);
                    HSSFCell cell = row.getCell(1);
                    HSSFCell cell2 = row.getCell(3);
                    map.put("layout", "");
                    while (row != null && cell != null && !cell.getStringCellValue().equals("")) {
                        cell = row.getCell(1);
                        cell2 = row.getCell(3);
                        if (cell != null && cell2 != null) {
                            String c2 = cell2.getStringCellValue() + "--" + cell.getStringCellValue();
                            map.put(index - 1 + "", c2);
                        }
                        index++;
                        row = sheet2.getRow(index);
                    }

                    File file = new File(hsFile.getAbsoluteFile() + "\\" + hsFile.getName() + ".xls");
                    if (!file.exists()) {
                        file.createNewFile();
                    }else{
                        file.delete();
                    }
                    System.out.println(file.getName());
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    File[] pics = hsFile.listFiles();
                    for (File pic : pics) {
                        try {
                            if (pic.getName().indexOf("win10")>=0) {
                                String picName = pic.getName().substring(pic.getName().lastIndexOf("_") + 1, pic.getName().lastIndexOf("."));
                                System.out.println(picName);
                                HSSFSheet sheet = workbook.createSheet(picName);
                                addPic(workbook, sheet, pic, map);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    List<Integer> sheets = new LinkedList<Integer>();
                    workbook.sheetIterator().forEachRemaining(s -> {
                        if (!s.getSheetName().equals("layout")) {
                            sheets.add(Integer.parseInt(s.getSheetName()));
                        }
                    });
                    Integer[] a = new Integer[] {};
                    Integer[] sheetName = sheets.toArray(a);
                    Arrays.sort(sheetName);
                    int i = 0;
                    for (Object name : sheetName) {
                        workbook.setSheetOrder(name.toString(), i++);
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

    private static void addPic(HSSFWorkbook workbook, HSSFSheet sheet, File pic, Map<String, String> map)
            throws IOException {
        File edgeFile = new File(pic.getAbsolutePath().replace("win10", "win8.1"));
        File CT8FIle = new File(pic.getAbsolutePath().replace("win10", "win7"));
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(map.get(sheet.getSheetName()));
        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("win10");

        BufferedImage image = ImageIO.read(pic);
        image = ImageUtils.zoomByScale(image);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOut);
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 5, (short) (image.getWidth() / 64),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        int col = image.getWidth() / 64 + 1;
        byteArrayOut.close();

        cell = row.createCell(col);
        cell.setCellValue("win8.1");
        byteArrayOut = new ByteArrayOutputStream();
        image = ImageIO.read(edgeFile);
        image = ImageUtils.zoomByScale(image, 1.0);
        ImageIO.write(image, "png", byteArrayOut);
        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (col), 5,
                (short) (image.getWidth() / 64 + col),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        col = col + image.getWidth() / 64 + 1;
        byteArrayOut.close();

        cell = row.createCell(col);
        cell.setCellValue("win7");
        byteArrayOut = new ByteArrayOutputStream();
        image = ImageIO.read(CT8FIle);

        image = ImageUtils.zoomByScale(image,1.0);

        ImageIO.write(image, "png", byteArrayOut);
        anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) (col), 5,
                (short) (image.getWidth() / 64 + col),
                image.getHeight() / 18);
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        byteArrayOut.close();
    }


}
