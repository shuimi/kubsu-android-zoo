package com.example.zoo;

import android.app.Application;

import androidx.room.Room;

import com.example.zoo.db.AppDatabase;

public class ZooApp extends Application {
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = Room
                .databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "database"
                )
                .allowMainThreadQueries()
                .build();
    }

}
