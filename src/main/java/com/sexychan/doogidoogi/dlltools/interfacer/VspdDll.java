package com.sexychan.doogidoogi.dlltools.interfacer;


import com.sexychan.doogidoogi.dlltools.DllUtil;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface VspdDll extends Library {

    VspdDll INSTANCE = (VspdDll) Native.loadLibrary(DllUtil.VSPD_DLL, VspdDll.class);

    boolean CreatePair(String comName1, String comName2);

    boolean DeletePair(String comName);

    boolean DeleteAll();
}
