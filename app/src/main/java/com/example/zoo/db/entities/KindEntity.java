package com.example.zoo.db.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "kinds", foreignKeys = {
        @ForeignKey(entity = ZooEntity.class, parentColumns = "id", childColumns = "zooId", onDelete = CASCADE)
})
public class KindEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "zooId")
    public Long zooId;

    @ColumnInfo(name = "kind_name")
    public String kindName;

    @ColumnInfo(name = "aviary_number")
    public Integer aviaryNumber;
}
