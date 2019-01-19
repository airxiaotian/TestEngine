package com.hitachi.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LogAnalyze extends JFrame {
    List<String> data = new LinkedList<String>();

    public static void main(String[] args) {
        LogAnalyze log = new LogAnalyze();
        log.setVisible(true);
    }

    public LogAnalyze() {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JList<String> list = new JList<String>();
        JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        scroll.setViewportView(list);
        this.add(scroll);
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("start");
        JMenuItem item = new JMenuItem("read");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.setListData(data.toArray(new String[] {}));
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(scroll);
                File file = chooser.getSelectedFile();
                String curCase = "";
                String curLayout = "";
                int fatal = 0;
                String fatalMessage = "";
                String message = "";
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        if (line.indexOf("ケース番号") != -1) {
                            if (fatal != 0) {
                                message = curLayout + " " + curCase + " " + "FATAL:" + fatal + " " + fatalMessage;
                                data.add(data.size() + 1 + " " + message);
                                list.setListData(data.toArray(new String[] {}));
                            }
                            fatal = 0;
                            fatalMessage = "";
                            curCase = line.split(" ")[2];
                        }
                        if (line.indexOf("画面番号") != -1) {
                            if (fatal != 0) {
                                message = curLayout + " " + curCase + " " + "FATAL:" + fatal + " " + fatalMessage;
                                data.add(data.size() + 1 + " " +message);
                                list.setListData(data.toArray(new String[] {}));
                            }
                            fatal = 0;
                            fatalMessage = "";
                            curLayout = line.split(" ")[2];
                        }
                        if (line.indexOf("FATAL") != -1) {
                            fatal++;
                            for (int i = 2; i < line.split(" ").length; i++) {
                                fatalMessage += line.split(" ")[i] + " ";
                            }
                        }
                    }
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        menu.add(item);
        bar.add(menu);
        this.setJMenuBar(bar);
    }

}
