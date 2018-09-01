package com.jatinjha.joshfeeds.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    @SerializedName("posts")
    List<Item> itemPosts;

    public List<Item> getItemPosts() {
        return itemPosts;
    }

    public void setItemPosts(List<Item> itemPosts) {
        this.itemPosts = itemPosts;
    }
}
