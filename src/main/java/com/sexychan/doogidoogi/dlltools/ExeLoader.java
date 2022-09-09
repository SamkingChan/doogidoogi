package com.sexychan.doogidoogi.dlltools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * dll加载工具类
 */
@Slf4j
public class ExeLoader {
    public static void loadExe() {
        try {
            String resourcePath = "/driver/";
            String[] fileNames = {"evsbc.cat", "evsbc.inf", "evsbc.sys", "evserial.cat", "evserial.inf", "evserial.sys", "vsbsetup.exe"};
            String folderName = System.getProperty("java.io.tmpdir") + "/driver/";
            File folder = new File(folderName);
            folder.mkdirs();
            File exeFile = null;
            for (int i = 0; i < fileNames.length; i++) {
                File libFile = new File(folder, fileNames[i]);
                log.info("将把驱动文件" + fileNames[i] + "注入在：" + libFile.getAbsolutePath());
                if (libFile.exists()) {
                    log.info("驱动文件已存在于" + libFile.getAbsolutePath());
                } else {
                    InputStream in = ExeLoader.class.getResourceAsStream(resourcePath + fileNames[i]);
                    FileUtils.copyInputStreamToFile(in, libFile);
                    log.info("驱动文件" + fileNames[i] + "注入成功！");
                    in.close();
                    log.info("加载驱动文件" + fileNames[i] + "成功！");
                    if (fileNames[i].equals("vsbsetup.exe")) {
                        exeFile = libFile;
                    }
                }
            }
            if (exeFile != null) {
                String absolutePath = exeFile.getAbsolutePath();
                String programPath = absolutePath.substring(0, absolutePath.lastIndexOf("\\"));
                log.info("正在启动");
                Runtime runtime = Runtime.getRuntime();
                String command = "cmd.exe /c C: && cd " + programPath + " && vsbsetup.exe";
                log.info("正在执行" + command + "指令安装驱动.....");
                Process exec = runtime.exec(command);
                exec.waitFor();
                log.info("驱动安装完毕！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load required lib", e);
        }
    }

    public static void loadExeDriver() {
        try {
            String resourcePath = "/driver/";
            String[] fileNames = {"evsbc.cat", "evsbc.inf", "evsbc.sys", "evserial.cat", "evserial.inf", "evserial.sys", "vsbsetup.exe"};
            String folderName = System.getProperty("java.io.tmpdir") + "/driver/";
            File folder = new File(folderName);
            folder.mkdirs();
            File exeFile = null;
            for (int i = 0; i < fileNames.length; i++) {
                File libFile = new File(folder, fileNames[i]);
                log.info("重新将把驱动文件" + fileNames[i] + "注入在：" + libFile.getAbsolutePath());
                if (libFile.exists()) {
                    log.info("驱动文件已存在于" + libFile.getAbsolutePath() + "，执行删除后重建");
                    boolean delete = libFile.delete();
                    if (delete) {
                        log.info("删除成功");
                    } else {
                        log.info("删除失败");
                    }
                }
                InputStream in = ExeLoader.class.getResourceAsStream(resourcePath + fileNames[i]);
                FileUtils.copyInputStreamToFile(in, libFile);
                log.info("驱动文件" + fileNames[i] + "注入成功！");
                in.close();
                log.info("加载驱动文件" + fileNames[i] + "成功！");
                if (fileNames[i].equals("vsbsetup.exe")) {
                    exeFile = libFile;
                }
            }
            if (exeFile != null) {
                String absolutePath = exeFile.getAbsolutePath();
                String programPath = absolutePath.substring(0, absolutePath.lastIndexOf("\\"));
                log.info("正在启动");
                Runtime runtime = Runtime.getRuntime();
                String command = "cmd.exe /c C: && cd " + programPath + " && vsbsetup.exe";
                log.info("正在执行" + command + "指令安装驱动.....");
                Process exec = runtime.exec(command);
                exec.waitFor();
                log.info("驱动安装完毕！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load required lib", e);
        }
    }
}
