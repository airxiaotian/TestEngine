package com.hitachi.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.StringUtil;
import org.apache.tools.ant.util.StringUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class TestCode {
    public static void main(String[] args) {
        //        try {
        //            ZipFile zipFile = new ZipFile(new File("D:\\FTP_TMPDIR\\CORINSUpdate\\ct181023_01haishin.zip"));
        //            zipFile.setPassword("2dml9100");
        //            List<FileHeader> fileHeaders =   zipFile.getFileHeaders();
        //            System.out.println(fileHeaders.get(0).getFileName());
        ////            zipFile.extractAll("D:\\FTP_TMPDIR\\CORINSUpdate");
        //        } catch (ZipException e) {
        //            // TODO 自動生成された catch ブロック
        //            e.printStackTrace();
        //        }
        int i =0;
        Map<Integer, String> map = new HashMap<Integer, String>();
        File evidence_new = new File("D:\\bbb\\JCIS");
        File[] dirs = evidence_new.listFiles();
        for (File dir : dirs) {
            //                        File hsFile = new File("D:\\JCIS分\\完了\\" + dir.getName() + "_2_画面項目定義書.xls");
            //                        try {
            //                            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(hsFile));
            //                            HSSFSheet sheet = workbook.getSheetAt(0);
            //                            int index = 2;
            //                            HSSFRow row = sheet.getRow(index);
            //                            HSSFCell cell = row.getCell(1);
            //                            while (cell != null && !cell.getStringCellValue().equals("")) {
            //
            //                                String c2 = cell.getStringCellValue();
            //                                map.put(index-1, c2);
            //                                index++;
            //                                row = sheet.getRow(index);
            //                                cell = row.getCell(1);
            //                            }
            //
            //                        } catch (FileNotFoundException e) {
            //                            // TODO 自動生成された catch ブロック
            //                            e.printStackTrace();
            //                        } catch (IOException e) {
            //                            // TODO 自動生成された catch ブロック
            //                            e.printStackTrace();
            //                        }
//                                    map.forEach((k, v) -> {
//                                        System.out.println(k + "    " + v);
//                                    });
            File[] files = dir.listFiles();
            for (File file : files) {

                if (file.getName().startsWith("win10")) {
                    i++;
                }
//                    file.delete();
//                    file.renameTo(new File(file.getAbsolutePath().replace("ie_", "win7_")));
//                }
            }
            System.out.println(i);
        }
    }

}
