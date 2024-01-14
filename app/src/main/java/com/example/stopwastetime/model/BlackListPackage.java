package com.example.stopwastetime.model;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Package")
public class BlackListPackage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "nomePackage")
    String packageName;

    public BlackListPackage(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object foreground) { //fatto per il contains nel servizio, in modo tale da evitare il foreach
        if (foreground instanceof BlackListPackage) {
            return this.packageName.equals(((BlackListPackage)foreground).packageName);
        }
        return false;
    }

    @Override //override del hashcode se fatto l'override di equals; non mi serviva ma è una buona pratica
    public int hashCode() {
        return Objects.hash(packageName);
    }
}
