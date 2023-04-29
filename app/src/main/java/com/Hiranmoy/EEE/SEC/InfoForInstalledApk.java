package com.Hiranmoy.EEE.SEC;

import android.graphics.drawable.Drawable;

class InfoForInstalledApk {
    String packageName, location, appName, date;
    Drawable icon;
    double apkSize;

    public InfoForInstalledApk(String packageName, String location, String appName, Drawable icon, double apkSize) {
        this.packageName = packageName;
        this.location = location;
        this.appName = appName;
        this.icon = icon;
        this.apkSize = apkSize;
        date = "0";
    }
    public InfoForInstalledApk(String packageName, String location, String appName, Drawable icon, double apkSize, String date) {
        this.packageName = packageName;
        this.location = location;
        this.appName = appName;
        this.icon = icon;
        this.apkSize = apkSize;
        this.date = date;
    }
}
