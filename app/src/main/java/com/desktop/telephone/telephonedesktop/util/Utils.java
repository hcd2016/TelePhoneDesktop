package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.base.App;

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

    public static final int getColor(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(App.getContext(), id);
        } else {
            return App.getContext().getResources().getColor(id);
        }
    }

    private static Toast toast = null;
    public static void Toast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
