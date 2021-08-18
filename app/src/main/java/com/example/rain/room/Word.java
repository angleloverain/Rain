package com.example.rain.room;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Word {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "region")
    public String region;

    @ColumnInfo(defaultValue = "0",name = "age")
    public int age;

    @Ignore // 忽略
    public Drawable drawable;
}
