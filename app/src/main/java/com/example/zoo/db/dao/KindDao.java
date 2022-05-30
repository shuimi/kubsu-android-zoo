package com.example.zoo.db.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zoo.db.entities.KindEntity;

import java.util.List;
import java.util.Optional;

@Dao
public interface KindDao {
    @Query("SELECT * FROM kinds")
    List<KindEntity> findAll();

    @Query("SELECT * FROM kinds")
    LiveData<List<KindEntity>> findAllReactive();

    @Query("SELECT * FROM kinds WHERE id = :id")
    Optional<KindEntity> findById(Long id);

    @Query("SELECT * FROM kinds WHERE zooId = :zooId")
    LiveData<List<KindEntity>> findAllByZooIdReactive(Long zooId);

    @Query("SELECT * FROM kinds WHERE zooId = :zooId")
    List<KindEntity> findAllByZooId(Long zooId);

    @Insert(onConflict = IGNORE)
    void insert(KindEntity kind);

    @Update(onConflict = REPLACE)
    void save(KindEntity kind);

    @Query("DELETE FROM kinds WHERE id = :id")
    void delete(Long id);
}
