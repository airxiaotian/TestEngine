package com.hitachi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CheckOK {
    public static void main(String[] args) {
        File dir = new File("D:\\CT81");
        for (File hsFile : dir.listFiles()) {
            try {
                System.out.println(hsFile);
                XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(hsFile));
                XSSFSheet sheet2 = workbook.getSheetAt(0);
                int index = 2;
                XSSFRow row = sheet2.getRow(index++);
                sheet2.getRow(1).getCell(11).setCellValue("Win8.1");
                sheet2.getRow(1).getCell(12).setCellValue("Win10");
                for (int i = 13; i < 19; i++) {
                    XSSFCellStyle style = row.getCell(1).getCellStyle();
                    style.setBorderBottom(BorderStyle.NONE);
                    style.setBorderTop(BorderStyle.NONE);
                    style.setBorderLeft(BorderStyle.NONE);
                    style.setBorderRight(BorderStyle.NONE);
                    sheet2.getRow(1).getCell(i).setCellStyle(style);
                    sheet2.getRow(1).getCell(i).setCellValue("");
                }
                while (row != null) {
                    XSSFCellStyle style = row.getCell(1).getCellStyle();
                    style.setBorderBottom(BorderStyle.NONE);
                    style.setBorderTop(BorderStyle.NONE);
                    style.setBorderLeft(BorderStyle.NONE);
                    style.setBorderRight(BorderStyle.NONE);
                    for (int i = 11; i < 13; i++) {
                        XSSFCell cell = row.getCell(i);
                        if(cell == null)
                            cell = row.createCell(i);
                        if(row.getCell(1)!=null&&!row.getCell(1).getStringCellValue().equals(""))
//                        cell.setCellStyle(style);
                        cell.setCellValue("OK");
                    }
                    for (int i = 13; i < 19; i++) {
                        XSSFCell cell = row.getCell(i);
                        if(cell == null)
                            continue;
                        cell.setCellStyle(style);
                        cell.setCellValue("");
                    }
                    row = sheet2.getRow(index++);
                }
                FileOutputStream output = new FileOutputStream(hsFile);
                workbook.write(output);
                output.close();
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
