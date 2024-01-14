package com.example.stopwastetime.model;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class DialogIcons {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "appName")
    public String appName;
    @ColumnInfo(name = "icon")
    public Drawable icon;
    @ColumnInfo(name = "packageName")
    public String packageName;

    public DialogIcons(String appName, Drawable icon, String packageName) {
        this.appName = appName;
        this.icon = icon;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
