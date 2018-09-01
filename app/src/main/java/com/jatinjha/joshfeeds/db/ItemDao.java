package com.jatinjha.joshfeeds.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jatinjha.joshfeeds.model.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
     void addItem(Item item);

    @Query("select * from Item")
    List<Item> getAll();

    @Query("DELETE FROM Item")
    public void nukeTable();

}
