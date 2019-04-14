package com.example.traveler.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by wty on 2018/7/18.
 */

public class JudgePermissionUtil {

    public static boolean judgePermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static void setPermission(Activity activity, String[] permissionList) {
        ActivityCompat.requestPermissions(activity, permissionList, 1);
    }


}
