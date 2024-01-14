package com.example.stopwastetime.model;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blacklist")
public class BlackList {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "nome")
    String name;
    @ColumnInfo(name = "image")
    Drawable image;

    public BlackList(String name, Drawable image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
