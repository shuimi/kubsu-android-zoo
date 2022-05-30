package com.example.zoo.db.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "animals", foreignKeys = {
        @ForeignKey(entity = KindEntity.class, parentColumns = "id", childColumns = "kindId", onDelete = CASCADE)
})
public class AnimalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "kindId")
    public Long kindId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "aviary")
    public Integer aviary;
}
