package com.hitachi.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tools.ant.util.FileUtils;

public class TestCode {
    public static void main(String[] args) {
        File file = new File("D:\\evidence_layout");
        File[] dirs = file.listFiles();
        for (File dir : dirs) {
            for (File layout : dir.listFiles()) {
                if (layout.getName().endsWith("png")) {
                    try {
                        copy(layout, new File(file.getAbsolutePath() + "\\" + dir.getName() + "_" + layout.getName()));
                    } catch (FileNotFoundException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void copy(File src, File dst) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(src);
        if (!dst.exists()) {
            dst.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        int size = 0;
        int index = 0;
        do {
            size = in.read(buffer, index * 1024, 1024);
            out.write(buffer, index * 1024, size);
        } while (size >= 1024);
    }
}
