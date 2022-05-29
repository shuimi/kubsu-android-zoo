package com.example.zoo.db;

import androidx.room.RoomDatabase;

import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;

public abstract class Database extends RoomDatabase {
    public abstract ZooDao getZooDao();
    public abstract KindDao getKindDao();
    public abstract AnimalDao getAnimalDao();
}
