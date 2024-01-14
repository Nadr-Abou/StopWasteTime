package com.example.stopwastetime.interfaccie;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.stopwastetime.model.BlackList;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BlackListDao {
    @Insert
    void insertBlackList(BlackList blackList);

    @Delete
    void deleteBlackList(BlackList blackList);

    @Query("SELECT * FROM blacklist")
    List<BlackList> getAll();
}
