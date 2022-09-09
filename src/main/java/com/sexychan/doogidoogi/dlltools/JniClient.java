package com.sexychan.doogidoogi.dlltools;


import com.sun.jna.Platform;

/**
 * jni加载
 */
public class JniClient {

    public native int sexyChan(String arg);

    public JniClient() {
        String suffix;
        if (Platform.isLinux()) {
            suffix = ".so";
        } else if (Platform.isWindows()) {
            suffix = ".dll";
        } else {
            suffix = "";
        }
        LibLoader.loadLib(DllUtil.VSPD_DLL + suffix);
    }

}
