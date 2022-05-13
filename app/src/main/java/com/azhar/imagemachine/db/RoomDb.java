package com.azhar.imagemachine.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.azhar.imagemachine.models.ImageModel;
import com.azhar.imagemachine.models.Machine;

@Database(entities = {Machine.class, ImageModel.class}, version = 3, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {
    public abstract MachineDao machineDao();
}