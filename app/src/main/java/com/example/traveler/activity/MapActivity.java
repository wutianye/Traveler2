package com.example.traveler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.traveler.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    /**
     * 高德地图
     *
     * @param mContext
     * @param lat      纬度
     * @param lng      经度
     */
    private static void gaoDe(Context mContext, String lat, String lng, String adr) {
        if (isAvilible(mContext, "com.autonavi.minimap")) {
            try {
                String url = "amapuri://route/plan/?sid=BGVIS1&slat=&slon=&sname=&did=&dlat=" + lat + "&dlon=" + lng + "&dname=" + adr + "&dev=0&t=0";
                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(url));
                mContext.startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("error", "您未安装高德地图");
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }
    }

    /* 检查手机上是否安装了指定的软件
   * @param context
   * @param packageName：应用包名
   * @return
   */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        gaoDe(this, "117", "117", "栖霞山");
    }

}
