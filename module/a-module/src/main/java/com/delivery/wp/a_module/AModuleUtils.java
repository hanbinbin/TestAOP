package com.delivery.wp.a_module;

import com.model.binbin.mylibrary.AppUtils;

public class AModuleUtils {

    public static String getName(){
        return AppUtils.getLibraryName();
    }

    public static String getVersion(){
        return AppUtils.getLibraryVersion();
    }
}
