package com.azhar.imagemachine.db;

import android.content.Context;

import androidx.room.Room;


public class DbClient {

    private Context context;
    private static DbClient mInstance;

    private RoomDb appRoomDb;

    private DbClient(Context context) {
        this.context = context;

        appRoomDb = Room.databaseBuilder(context, RoomDb.class, "db_machine").fallbackToDestructiveMigration().build();

    }

    public static synchronized DbClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DbClient(mCtx);
        }
        return mInstance;
    }

    public RoomDb getAppDatabase() {
        return appRoomDb;
    }
}
