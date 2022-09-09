package com.sexychan.doogidoogi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Author: SexyChan
 * @description:
 * @date: 2022-09-08 17:35
 */
@Slf4j
@Component
public class SwingGUI {

    @Resource
    @Lazy
    MidiUtils midiUtils;

    JPanel panel;
    JFrame frame;
    @Value("${midi.device}")
    String device = "USB Midi";

    public void createAndShowGUI() {
        try {
            // 创建 JFrame 实例
            frame = new JFrame("超级架子鼓IO板虚拟程序");

            //创建面板，这个类似于 HTML 的 div 标签
            //我们可以创建多个面板并在 JFrame 中指定位置
            //面板中我们可以添加文本字段，按钮及其他组件。
            panel = new JPanel();

            //设置窗口大小
            frame.setSize(350, 150);

            //下边的这句话，如果这么写的话，窗口关闭，springboot项目就会关掉，使用dispose则不会
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispose(); //如果写这句可实现窗口关闭，springboot项目仍运行

            panel.setLayout(null);

            // 创建 JLabel
            JLabel startLabel = new JLabel("程序正在加载中...");
            startLabel.setBounds(10, 20, 120, 25);
            panel.add(startLabel);
            // 添加面板
            frame.add(panel);

            //居中
            frame.setLocationRelativeTo(null);

            // 设置界面可见
            frame.setVisible(true);

            //设置图标
            Image image = new ImageIcon(SwingGUI.class.getClassLoader().getResource("jazzdrum.png")).getImage();
            frame.setIconImage(image);

            //设置托盘图标
            TrayIcon trayIcon = new TrayIcon(image);

            //创建托盘图标对象
            SystemTray systemTray = SystemTray.getSystemTray();

            //设置托盘图标大小自适应
            trayIcon.setImageAutoSize(true);
            PopupMenu popup = new PopupMenu();
            MenuItem showItem = new MenuItem("打开");
            MenuItem exitItem = new MenuItem("退出");
            showItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setExtendedState(0);
                    frame.setVisible(true);
                }
            });
            exitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }

            });
            popup.add(showItem);
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            systemTray.add(trayIcon);

            //窗口监听
            frame.addWindowListener(new WindowAdapter() {
                //窗口关闭事件
                @Override
                public void windowClosing(WindowEvent e) {
                    //设置窗口不可见
                    frame.setVisible(false);
                }
            });

            //鼠标监听
            trayIcon.addMouseListener(new MouseAdapter() {
                //鼠标点击事件
                @Override
                public void mouseClicked(MouseEvent e) {
                    //鼠标左键事件
                    if (e.getButton() == 1) {
                        //鼠标左键点击托盘图标，恢复原窗口
                        frame.setVisible(true);
                    }
                    //托盘图标消失
                    //systemTray.remove(trayIcon);
                }
            });
        } catch (Exception e) {
            log.info("出现异常：" + e.getMessage());
        }
    }

    public void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("请输入MIDI设备名称:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10, 20, 120, 25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField midiPortText = new JTextField(20);
        midiPortText.setBounds(140, 20, 165, 25);
        midiPortText.setText(device);
        panel.add(midiPortText);

        // 输入密码的文本域
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setBounds(10, 50, 80, 25);
//        panel.add(passwordLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
//        JPasswordField passwordText = new JPasswordField(20);
//        passwordText.setBounds(100, 50, 165, 25);
//        panel.add(passwordText);

        // 创建登录按钮
        JButton connect = new JButton("连接");
        connect.setBounds(85, 65, 80, 25);
        panel.add(connect);

        JButton disconnect = new JButton("断开");
        disconnect.setBounds(185, 65, 80, 25);
        disconnect.setEnabled(false);
        panel.add(disconnect);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String midiPort = midiPortText.getText().trim();
                if (StringUtils.isBlank(midiPort)) {
                    JOptionPane.showMessageDialog(null, "请输入MIDI设备名称！","提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    midiUtils.setDevice(midiPort);
                    ResultApi resultApi = midiUtils.openMidiDevice();
                    if(resultApi.getCode().equals(1)){
                        connect.setEnabled(false);
                        disconnect.setEnabled(true);
                    }
                    JOptionPane.showMessageDialog(null, resultApi.getMessage(),"提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        disconnect.addActionListener(e -> {
            midiUtils.closeMidi();
            connect.setEnabled(true);
            disconnect.setEnabled(false);
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }
}
