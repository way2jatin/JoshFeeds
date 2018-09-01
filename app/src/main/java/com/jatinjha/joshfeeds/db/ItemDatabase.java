package com.jatinjha.joshfeeds.db;


import android.arch.persistence.room.RoomDatabase;

import com.jatinjha.joshfeeds.model.Item;


@android.arch.persistence.room.Database(entities = {Item.class},version = 1)
public abstract  class ItemDatabase extends RoomDatabase {

    public abstract ItemDao myDao();

}
