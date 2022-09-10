package com.sexychan.doogidoogi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.Component.RIGHT_ALIGNMENT;

/**
 * @Author: SexyChan
 * @description:
 * @date: 2022-09-08 17:35
 */
@Slf4j
@Component
public class SwingGUI {

    @Resource
    PropertiesConfiguration propertiesConfiguration;
    @Resource
    @Lazy
    MidiUtils midiUtils;
    JPanel panel;
    JFrame frame;
    @Value("${midi.device}")
    String device = "USB Midi";

    JTextArea textArea;

    JButton connect;
    JScrollPane scroll;


    public void createAndShowGUI() {
        try {
            // 创建 JFrame 实例
            frame = new JFrame("超级架子鼓IO板虚拟程序");

            //创建面板，这个类似于 HTML 的 div 标签
            //我们可以创建多个面板并在 JFrame 中指定位置
            //面板中我们可以添加文本字段，按钮及其他组件。
            panel = new JPanel();

            //设置窗口大小
            frame.setSize(375, 235);

            //下边的这句话，如果这么写的话，窗口关闭，springboot项目就会关掉，使用dispose则不会
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispose(); //如果写这句可实现窗口关闭，springboot项目仍运行

            // 添加面板
            frame.add(panel);
            placeComponents(panel);
            createTextArea();
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

    public void createTextArea() {
        textArea = new JTextArea();
//        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("宋体", Font.PLAIN, 12));
//        textArea.setWrapStyleWord(true);
        textArea.append("-欢迎使用超级架子鼓IO板虚拟程序-\n");
        scroll = new JScrollPane();
        scroll.getViewport().add(textArea);
        scroll.setBounds(140, 10, 210, 180);
        panel.add(scroll);
    }

    public void appendText(String text) {
        textArea.append("-"+text);
        textArea.append("\n");
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    public void placeComponents(JPanel panel) {
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("超级架子鼓");
        userLabel.setBounds(10, 10, 120, 25);
        userLabel.setFont(new Font("宋体", Font.BOLD, 20));
        panel.add(userLabel);

        // 创建 JLabel
        JLabel sexyLabel = new JLabel("        --SexyChan");
        sexyLabel.setBounds(10, 30, 120, 25);
        sexyLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        panel.add(sexyLabel);

        // 创建 JLabel
        JLabel midiName = new JLabel("输入MIDI设备名称:");
        midiName.setBounds(10, 30 + 20 + 10, 120, 25);
        panel.add(midiName);

        //创建文本域用于用户输入
        JTextField midiPortText = new JTextField(20);
        midiPortText.setBounds(10, 20 + 25 + 10 + 20 + 10, 120, 25);
        midiPortText.setText(device);
        panel.add(midiPortText);

        // 创建登录按钮
        connect = new JButton("连接");
        connect.setBounds(30, 20 + 25 + 25 + 10 + 25 + 20, 80, 25);
        connect.setEnabled(false);
        panel.add(connect);

        JButton disconnect = new JButton("断开");
        disconnect.setBounds(30, 20 + 25 + 25 + 25 + 10 + 10 + 25 + 15, 80, 25);
        disconnect.setEnabled(false);
        panel.add(disconnect);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String midiPort = midiPortText.getText().trim();
                if (StringUtils.isBlank(midiPort)) {
                    JOptionPane.showMessageDialog(null, "请输入MIDI设备名称！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    propertiesConfiguration.setProperty("midi.device", midiPort);
                    midiUtils.setDevice(midiPort);
                    ResultApi resultApi = midiUtils.openMidiDevice();
                    if (resultApi.getCode().equals(1)) {
                        connect.setEnabled(false);
                        disconnect.setEnabled(true);
                    }
                    JOptionPane.showMessageDialog(null, resultApi.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
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

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setConnectButtonEnable() {
        connect.setEnabled(true);
    }
}
