package com.sexychan.doogidoogi.utils;

import com.sexychan.doogidoogi.dlltools.ExeLoader;
import com.sexychan.doogidoogi.dlltools.JniClient;
import com.sexychan.doogidoogi.dlltools.interfacer.VspdDll;
import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;

@Component
@Slf4j
@EnableScheduling
public class SerialTools implements CommandLineRunner {
    NRSerialPort nrSerialPort;
    OutputStream outputStream;

    @Value("${com.port}")
    String port = "COM2";

    @Value("${midi.device}")
    String device;

    @Resource
    PropertiesConfiguration propertiesConfiguration;
    @Resource
    SwingGUI swingGUI;
    String password;

    Integer connectCount = 0;

    @Override
    public void run(String... args) {
        password = propertiesConfiguration.getString("password");
        swingGUI.createAndShowGUI();
        new JniClient();
        starter();
    }

    public void starter() {
        try {
            swingGUI.appendText("虚拟串口连接中...");
            log.info("虚拟串口连接中...");
            nrSerialPort = new NRSerialPort(port, 9600);
            boolean connect = nrSerialPort.connect();
            if (connect) {
                swingGUI.appendText("串口" + port + "连接成功");
                log.info("串口" + port + "连接成功");
                outputStream = nrSerialPort.getOutputStream();
                swingGUI.setConnectButtonEnable();
                connectCount = 0;
            } else {
                swingGUI.appendText("串口" + port + "连接失败,尝试安装驱动...");
                log.info("串口" + port + "连接失败,尝试安装驱动...");
                ExeLoader.loadExeDriver();
                boolean deleteAll = VspdDll.INSTANCE.DeleteAll();
                if (deleteAll) {
                    swingGUI.appendText("虚拟串口删除成功");
                    log.info("虚拟串口删除成功");
                } else {
                    swingGUI.appendText("虚拟串口删除失败");
                    log.info("虚拟串口删除失败");
                }
                swingGUI.appendText("重新创建端口...");
                log.info("重新创建端口...");
                Thread.sleep(10 * 1000);
                boolean b = VspdDll.INSTANCE.CreatePair("COM1", "COM2");
                if (b) {
                    swingGUI.appendText("虚拟串口创建成功,尝试重连端口中...");
                    log.info("虚拟串口创建成功,尝试重连端口中...");
                    nrSerialPort = new NRSerialPort(port, 9600);
                    connect = nrSerialPort.connect();
                    if (connect) {
                        swingGUI.appendText("串口" + port + "连接成功");
                        log.info("串口" + port + "连接成功");
                        outputStream = nrSerialPort.getOutputStream();
                        swingGUI.setConnectButtonEnable();
                        connectCount = 0;
                    } else {
                        swingGUI.appendText("串口" + port + "连接失败");
                        log.info("串口" + port + "连接失败");
                        if (connectCount < 5) {
                            swingGUI.appendText("虚拟串口创建....");
                            log.info("虚拟串口创建....");
                            VspdDll.INSTANCE.CreatePair("COM1", "COM2");
                            starter();
                        } else {
                            swingGUI.appendText("串口连接失败，请重启电脑后重试！");
                            log.info("串口连接失败，请重启电脑后重试！");
                            JOptionPane.showMessageDialog(null, "串口连接失败，请重启电脑后重试！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    if (connectCount < 5) {
                        swingGUI.appendText("虚拟串口创建失败,尝试删除重装驱动....");
                        log.info("虚拟串口创建失败，尝试删除重装驱动....");
                        deleteAll = VspdDll.INSTANCE.DeleteAll();
                        if (deleteAll) {
                            swingGUI.appendText("虚拟串口删除成功");
                            log.info("虚拟串口删除成功");
                        } else {
                            swingGUI.appendText("虚拟串口删除失败");
                            log.info("虚拟串口删除失败");
                        }
                        ExeLoader.loadExeDriver();
                        starter();
                    } else {
                        swingGUI.appendText("串口连接失败，请重启电脑后重试！");
                        log.info("串口连接失败，请重启电脑后重试！");
                        JOptionPane.showMessageDialog(null, "串口连接失败，请重启电脑后重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            if (connectCount < 5) {
                swingGUI.appendText("连接串口异常，尝试删除重装驱动....");
                log.info("连接串口异常，尝试删除重装驱动....");
                boolean deleteAll = VspdDll.INSTANCE.DeleteAll();
                if (deleteAll) {
                    swingGUI.appendText("虚拟串口删除成功");
                    log.info("虚拟串口删除成功");
                } else {
                    swingGUI.appendText("虚拟串口删除失败");
                    log.info("虚拟串口删除失败");
                }
                ExeLoader.loadExeDriver();
                starter();
            } else {
                swingGUI.appendText("串口连接失败，请重启电脑后重试！");
                log.info("串口连接失败，请重启电脑后重试！");
                JOptionPane.showMessageDialog(null, "串口连接失败，请重启电脑后重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Scheduled(initialDelay = 30 * 60 * 1000, fixedRate = 30 * 60 * 1000)
    public void authForServer() {
        try {
            String s = device.replaceAll(" ", "-");
            Boolean result = Boolean.valueOf(HttpClientUtil.doGet("http://test.cs.zhensuo.tv/auth/f/getLicence?device=" + s));
            if (!result) {
                if (!"doogidoogi582042278".equals(password)) {
                    closeSerial();
                }
            }
        } catch (Exception e) {
            if (!"doogidoogi582042278".equals(password)) {
                closeSerial();
            }
        }
    }

    public void closeSerial() {
        if (nrSerialPort != null && nrSerialPort.isConnected()) {
            nrSerialPort.disconnect();
            JOptionPane.showMessageDialog(null, "试用结束，如要继续使用，请联系QQ：582042278", "提示", JOptionPane.INFORMATION_MESSAGE);
            log.info("试用结束，如要继续使用，请联系QQ：582042278");
            swingGUI.appendText("串口已关闭");
            log.info("串口已关闭");
        }

    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
