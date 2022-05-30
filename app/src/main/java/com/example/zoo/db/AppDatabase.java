package com.example.zoo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.zoo.db.dao.AnimalDao;
import com.example.zoo.db.dao.KindDao;
import com.example.zoo.db.dao.ZooDao;
import com.example.zoo.db.entities.AnimalEntity;
import com.example.zoo.db.entities.KindEntity;
import com.example.zoo.db.entities.ZooEntity;

@Database(
        entities = {AnimalEntity.class, KindEntity.class, ZooEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ZooDao getZooDao();
    public abstract KindDao getKindDao();
    public abstract AnimalDao getAnimalDao();
}
