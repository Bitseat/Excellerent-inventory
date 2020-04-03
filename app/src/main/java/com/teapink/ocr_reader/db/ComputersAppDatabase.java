package com.teapink.ocr_reader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.teapink.ocr_reader.db.entity.Computer;

@Database(entities = {Computer.class},version = 1)
public abstract class ComputersAppDatabase extends RoomDatabase {
    public abstract ComputerDAO getComputerDAO();

}
