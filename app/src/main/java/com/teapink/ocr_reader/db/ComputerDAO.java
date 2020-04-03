package com.teapink.ocr_reader.db;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.teapink.ocr_reader.db.entity.Computer;

import java.util.List;

@Dao
public interface ComputerDAO {
    @Insert
    public  long addComputer(Computer computer);

    @Update
    public void updateComputer(Computer computer);

    @Delete
    public void deleteComputer(Computer computer);

    @Query("select * from computers")
    public List<Computer> getComputers();

    @Query("select * from computers where computer_id ==:computerId")
    public Computer getComputer(long computerId);
}
