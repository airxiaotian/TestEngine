package com.hitachi.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestCode {
    public static void main(String[] args) {
        File file = new File("D:\\evidence_new");
        File[] dirs = file.listFiles();
        for (File dir : dirs) {
            for (File layout : dir.listFiles()) {
                if (layout.getName().endsWith("png")) {
                    File newFile = new File(file.getAbsolutePath() + "\\" + layout.getName());
                    try {
                        copy(layout, newFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
