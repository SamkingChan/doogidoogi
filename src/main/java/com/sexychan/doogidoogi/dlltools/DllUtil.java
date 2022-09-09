package com.sexychan.doogidoogi.dlltools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DllUtil{
    public static String VSPD_DLL;

    static {
        String arch = System.getProperty("os.arch");
        log.info("系统版本为："+arch);
        if ("amd64".equals(arch) || "64".equals(arch)) {
            VSPD_DLL = "vspdctl";
        } else if ("x86".equals(arch)  || "32".equals(arch)) {
            VSPD_DLL = "vspdctl";
        }
    }
}
