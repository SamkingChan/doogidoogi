package com.sexychan.doogidoogi.utils.view;


import javax.swing.*;
import java.awt.*;

public class ConfigFrame {

    public void show() {
        JFrame frame = new JFrame("Simulater Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setLayout(null);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(300, 300));

        JTextArea logTextArea = new JTextArea();
        JScrollPane logScrollPane = new JScrollPane();
        logScrollPane.setBounds(10, 10, 200, 200);
        logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        logScrollPane.getViewport().add(logTextArea);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(logScrollPane);
        panel.setBounds(0, 0, 290, 290);

        frame.add(BorderLayout.CENTER,panel);
        try {
            for (int i = 0; i < 100; i++) {
                // System.out.println(scanner.nextLine());
                logTextArea.append("scanner.nextLine()" + i);
                logTextArea.append("\n");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //使垂直滚动条自动向下滚动
                logScrollPane.getVerticalScrollBar().setValue(logScrollPane.getVerticalScrollBar().getMaximum());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}