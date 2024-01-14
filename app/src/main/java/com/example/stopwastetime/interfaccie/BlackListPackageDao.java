package com.example.stopwastetime.interfaccie;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.stopwastetime.model.BlackListPackage;
import com.example.stopwastetime.model.Excuses;

import java.util.List;
@Dao
public interface BlackListPackageDao {
    @Insert
    void insertPackage(BlackListPackage blPackage);

    @Delete
    void deletePackage(BlackListPackage blPackage);

    @Query("SELECT * FROM Package")
    List<BlackListPackage> getBlPackages();
}
