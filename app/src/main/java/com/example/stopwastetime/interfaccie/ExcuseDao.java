package com.example.stopwastetime.interfaccie;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.stopwastetime.model.Excuses;

import java.util.List;

@Dao
public interface ExcuseDao {
    @Insert
    void insertExcuse(Excuses excuse);

    @Delete
    void deleteExcuse(Excuses excuse);

    @Query("SELECT * FROM Excuses")
    List<Excuses> getExcuses();
}
