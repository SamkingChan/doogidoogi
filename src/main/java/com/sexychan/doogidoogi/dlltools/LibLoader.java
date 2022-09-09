package com.sexychan.doogidoogi.dlltools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * dll加载工具类
 */
@Slf4j
public class LibLoader {
    public static void loadLib(String libName) {
        try {

            String resourcePath = "/dll/" + libName;
            String folderName = System.getProperty("java.io.tmpdir") + "/lib/";
            File folder = new File(folderName);
            folder.mkdirs();
            File libFile = new File(folder, libName);
            log.info("程序启动,将把dll文件注入在：" + libFile.getAbsolutePath());
            if (libFile.exists()) {
                log.info("dll已存在于" + libFile.getAbsolutePath());
                System.load(libFile.getAbsolutePath());
                log.info("加载dll文件成功！");
            } else {
                InputStream in = LibLoader.class.getResourceAsStream(resourcePath);
                FileUtils.copyInputStreamToFile(in, libFile);
                log.info("dll文件注入成功！");
                in.close();
                System.load(libFile.getAbsolutePath());
                log.info("加载dll文件成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("加在dll时出错，请检查系统版本");
        }
    }
}
