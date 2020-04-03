package com.teapink.ocr_reader.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "computers")
public class Computer {


    @ColumnInfo(name="computer_name")
    private String name;

    @ColumnInfo(name="computer_serial")
    private String serial;

    @ColumnInfo(name="computer_id")
    @PrimaryKey(autoGenerate =true)
    private long id;


    @Ignore
    public Computer() {
    }



    public Computer(long id, String name, String serial) {

        this.name = name;
        this.serial = serial;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
