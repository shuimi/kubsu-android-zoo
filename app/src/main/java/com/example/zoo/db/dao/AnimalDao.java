package com.example.zoo.db.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zoo.db.entities.AnimalEntity;
import com.example.zoo.db.entities.KindEntity;

import java.util.List;
import java.util.Optional;

@Dao
public interface AnimalDao {
    @Query("SELECT * FROM animals")
    List<AnimalEntity> findAll();

    @Query("SELECT * FROM animals")
    LiveData<List<AnimalEntity>> findAllReactive();

    @Query("SELECT * FROM animals WHERE id = :id")
    Optional<AnimalEntity> findById(Long id);

    @Query("SELECT * FROM animals WHERE kindId = :kindId")
    LiveData<List<AnimalEntity>> findAllByKindIdReactive(Long kindId);

    @Insert(onConflict = IGNORE)
    void insert(AnimalEntity animal);

    @Update(onConflict = REPLACE)
    void save(AnimalEntity animal);

    @Query("DELETE FROM animals WHERE id = :id")
    void delete(Long id);
}
