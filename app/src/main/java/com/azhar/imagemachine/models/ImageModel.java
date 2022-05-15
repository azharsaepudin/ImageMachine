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
    public String machineId;

    @ColumnInfo(name = "photo_path")
    public String photo_path;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
