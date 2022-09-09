package com.sexychan.doogidoogi.utils;

import java.io.UnsupportedEncodingException;

public class PathUtil {

    public static String DLL_PATH;

    static {
        String path = (PathUtil.class.getResource("/").getPath()).replaceAll("%20", " ").substring(1).replace("/", "\\");
        try {
            DLL_PATH = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
