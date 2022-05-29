package com.example.zoo.db.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zoo.db.entities.ZooEntity;

import java.util.List;
import java.util.Optional;

@Dao
public interface ZooDao {
    @Query("SELECT * FROM zoos")
    List<ZooEntity> findAll();

    @Query("SELECT * FROM zoos")
    LiveData<List<ZooEntity>> findAllReactive();

    @Query("SELECT * FROM zoos WHERE id = :id")
    Optional<ZooEntity> findById(Long id);

    @Insert(onConflict = IGNORE)
    void insert(ZooEntity zoo);

    @Update(onConflict = REPLACE)
    void save(ZooEntity zoo);

    @Query("DELETE FROM zoos WHERE id = :id")
    void delete(Long id);
}
