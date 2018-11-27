package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;

public class Utils {
    /**
     * 包名直接跳转
     **/
    public static void startApp(Context context,String pkgName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
