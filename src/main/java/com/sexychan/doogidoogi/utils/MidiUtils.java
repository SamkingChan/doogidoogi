package com.sexychan.doogidoogi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sound.midi.*;
import javax.swing.*;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @Author: SexyChan
 * @description:
 * @date: 2022-09-08 20:23
 */
@Slf4j
@Component
public class MidiUtils {

    @Value("${midi.device}")
    String device = "USB Midi";
    MidiDevice midiDevice;

    @Resource
    SerialTools serialTools;

    @Resource
    SwingGUI swingGUI;

    JTextArea textArea;

    private OutputStream outputStream;

    public ResultApi openMidiDevice() {
        textArea = swingGUI.getTextArea();
        swingGUI.appendText("查找MIDI设备中....");
        log.info("查找MIDI设备中....");
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            swingGUI.appendText("设定的设备名称为：" + device + ",找到的设备名称为：" + info.getName());
            log.info("设定的设备名称为：" + device + ",找到的设备名称为：" + info.getName());
            String trim = info.getName().toString().trim();
            log.info("设定的设备：" + device + "与找到的设备" + trim + "匹配结果：" + trim.equals(device));
            if (trim.equals(device)) {
                log.info("设备" + device + "匹配成功！");
                try {
                    midiDevice = MidiSystem.getMidiDevice(info);
                    midiDevice.open();
                    Transmitter transmitter = midiDevice.getTransmitter();
                    transmitter.setReceiver(new MidiInputReceiver());
                    swingGUI.appendText("MIDI设备：" + device + " 连接成功！");
                    log.info("MIDI设备：" + device + " 连接成功！");
                    if (outputStream == null) {
                        outputStream = serialTools.getOutputStream();
                    }
                    return new ResultApi(1, "MIDI设备：" + device + " 连接成功！");
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                    swingGUI.appendText("设备连接异常，请重新连接！");
                    log.info("设备连接异常，请重新连接！");
                    return new ResultApi(-1, "设备连接异常，请重新连接！");
                }
            } else {
                swingGUI.appendText("设定的设备：" + device + " 与找到的设备：" + info.getName() + " 不匹配！");
                log.info("设定的设备：" + device + " 与找到的设备：" + info.getName() + " 不匹配！");
            }
        }
        return new ResultApi(2, "无匹配设备，请检查设备是否连接成功后重试！");
    }

    public void closeMidi() {
        if (midiDevice != null && midiDevice.isOpen()) {
            midiDevice.close();
        }
    }

    public class MidiInputReceiver implements Receiver {
        @Override
        public void send(MidiMessage msg, long timeStamp) {
            try {
                if (textArea == null) {
                    textArea = swingGUI.getTextArea();
                }
                byte[] aMsg = msg.getMessage();
                if (aMsg[0] != -2) {
                    log.info(Arrays.toString(aMsg));
                    if (aMsg[2] > 0 && aMsg[2] != 64) {
                        byte b = aMsg[1];
                        swingGUI.appendText("侦测到的MIDI信号：" + b);
                        log.info("侦测到的MIDI信号：" + b);
                        switch (b) {
                            case 36:
//                            System.out.println("地鼓");
                                outputStream.write(0x08);
                                break;
//                    1通 10 --- 50
                            case 50:
                            case 48:
//                            System.out.println("1通");
                                outputStream.write(0x10);
                                break;
//                    2通 20 --- 47
                            case 47:
                            case 45:
//                            System.out.println("2通");
                                outputStream.write(0x20);
                                break;
//                    3通 40 --- 41
                            case 41:
//                            System.out.println("3通");
                                outputStream.write(0x40);
                                Thread.sleep(50);
                                outputStream.write(0xFF);
                                break;
//                    吊擦1 01 --- 49
                            case 49:
//                            System.out.println("吊擦1");
                                outputStream.write(0x01);
                                break;
//                    吊擦2 80 --- 57
                            case 57:
//                            System.out.println("吊擦2");
//                                break;
//                    叮叮 80 --- 51
                            case 51:
                                outputStream.write(0x80);
//                            System.out.println("投币");
                                break;
//                    hihat 02 --- 46 44 42
                            case 42:
//                                outputStream.write(0x02);
//                            System.out.println("hihat");
                            case 44:
//                                outputStream.write(0x02);
//                            System.out.println("hihat");
                            case 46:
                            case 4:
                                outputStream.write(0x02);
//                            System.out.println("hihat");
                                break;
//                    hihat crtl 02
//                    军鼓04 --- 38
                            case 38:
//                            System.out.println("军鼓");
                                outputStream.write(0x04);
                                break;
//                    投币 FF
                            default:
                                outputStream.write(0xFF);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void close() {

        }
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
