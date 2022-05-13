package com.azhar.imagemachine.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tb_machine_image")
public class ImageModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int photoId;

    @ColumnInfo(name = "machine_id")
    public int machineName;

    @ColumnInfo(name = "photo_path")
    public String photo_path;

}
