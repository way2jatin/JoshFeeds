package com.jatinjha.joshfeeds.view;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jatinjha.joshfeeds.R;
import com.jatinjha.joshfeeds.adapter.FeedAdapter;
import com.jatinjha.joshfeeds.db.ItemDatabase;
import com.jatinjha.joshfeeds.model.Item;
import com.jatinjha.joshfeeds.model.Post;
import com.jatinjha.joshfeeds.utils.APIHelper;
import com.jatinjha.joshfeeds.utils.APIResponseListener;
import com.jatinjha.joshfeeds.utils.RVItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.jatinjha.joshfeeds.utils.AppConstant.URL1;
import static com.jatinjha.joshfeeds.utils.AppConstant.URL2;
import static com.jatinjha.joshfeeds.utils.AppConstant.URL3;

public class FeedActivity extends BaseAnimationActivity {

    RecyclerView recyclerView;

    FeedAdapter adapter;
    LinearLayoutManager layoutManager;

    int totalPage = 3, pageNo = 0;
    ArrayList<Item> items;
    ItemDatabase itemDatabase;
    private boolean isScrolling = false;
    private int pastVisibleItem, visibleItemCount, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initRecyclerView();
        items = new ArrayList<>();
        adapter = new FeedAdapter(this, items);
        recyclerView.setAdapter(adapter);

        itemDatabase = Room.databaseBuilder(this, ItemDatabase.class, "itemdb")
                .allowMainThreadQueries()
                .build();


        if (isInternetConnected()) {
            itemDatabase.myDao().nukeTable();
            loadDataOnline(++pageNo);
        } else {
            loadDataOffline();
        }

        recyclerView.addOnItemTouchListener(new RVItemClickListener(this, new RVItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(FeedActivity.this,FullScreenActivity.class).putExtra("image",items.get(position).getThumbnail_image()));
            }
        }));

    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();

                if (isScrolling && (visibleItemCount + pastVisibleItem) == totalItemCount && isInternetConnected()) {
                    isScrolling = false;
                    loadDataOnline(++pageNo);
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_main_likes:
                Comparator<Item> likesComp = new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getLikes() > o2.getLikes() ? 1 : -1;
                    }
                };
                sortData(likesComp);
                break;

            case R.id.menu_main_views:
                Comparator<Item> viewsComp = new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getViews() > o2.getViews() ? 1 : -1;
                    }
                };
                sortData(viewsComp);
                break;

            case R.id.menu_main_shares:
                Comparator<Item> shareComp = new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getShares() > o2.getShares() ? 1 : -1;
                    }
                };
                sortData(shareComp);
                break;

            case R.id.menu_main_date:

                Comparator<Item> dateComp = new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getEvent_date() > o2.getEvent_date() ? 1 : -1;
                    }
                };
                sortData(dateComp);
                break;
        }

        return true;
    }

    private void sortData(Comparator<Item> comparator) {
        Collections.sort(items, comparator);
        adapter.notifyDataSetChanged();
    }


    private void loadDataOffline() {
        items.clear();
        List<Item> list = itemDatabase.myDao().getAll();
        items.addAll(list);
        adapter.notifyDataSetChanged();
    }


    private void loadDataOnline(final int pageNo) {

        String url = "";

        switch (pageNo) {
            case 1:
                url = URL1;
                break;
            case 2:
                url = URL2;
                break;
            case 3:
                url = URL3;
                break;
        }

        if (pageNo <= totalPage) {
            APIHelper apiHelper = new APIHelper(this);
            apiHelper.callJsonWsGet(url,null,feedListener,true);
        }
    }

    private APIResponseListener feedListener = new APIResponseListener() {
        @Override
        public void handleResponse(String response) {
            try {
                Gson gson = new Gson();
                Post post = gson.fromJson(response,Post.class);
                for (Item item : post.getItemPosts()){
                    items.add(item);
                    itemDatabase.myDao().addItem(item);
                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleError(String response) {
            Toast.makeText(FeedActivity.this, "Something wrong happened.", Toast.LENGTH_SHORT).show();
        }
    };


    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }


}
