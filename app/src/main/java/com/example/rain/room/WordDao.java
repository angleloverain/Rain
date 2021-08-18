package com.example.rain.room;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Word... words);

    @Delete
    void delete(Word user);

    @Query("SELECT * FROM users")
    List<Word> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    List<Word> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    Word findByName(String first, String last);

    @Update
    public void updateWord(Word... words);

    @Query("delete from users")
    void deleteAll();

    @Query("SELECT * FROM users  WHERE region IN (:regions)")
    public LiveData<List<Word>> loadUsersFromRegionsSync(List<String> regions);

    // 访问游标，可以用来，兼容 ContentProvider
    @Query("SELECT * FROM users WHERE age > :minAge LIMIT 5")
    public Cursor loadRawUsersOlderThan(int minAge);
}
