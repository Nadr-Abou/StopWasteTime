package com.example.stopwastetime.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.stopwastetime.Converters;
import com.example.stopwastetime.interfaccie.BlackListDao;
import com.example.stopwastetime.interfaccie.BlackListPackageDao;
import com.example.stopwastetime.model.BlackList;
import com.example.stopwastetime.model.BlackListPackage;
import com.example.stopwastetime.model.Excuses;
import com.example.stopwastetime.interfaccie.ExcuseDao;

@Database(entities = {Excuses.class, BlackList.class, BlackListPackage.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            return Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "StopWasteTime")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract ExcuseDao excuseDao();
    public abstract BlackListDao blackListDao();
    public abstract BlackListPackageDao blackListPackageDao();

}


