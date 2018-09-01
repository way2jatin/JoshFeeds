package com.jatinjha.joshfeeds.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jatinjha.joshfeeds.R;
import com.jatinjha.joshfeeds.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    private Context context;

    private ArrayList<Item> items;

    public FeedAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent , false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder,  int position) {

        final Item item= items.get(position);

        Picasso.with(context)
                .load(item.getThumbnail_image())
                .into(holder.imageView);

        holder.likes.setText(String.valueOf(item.getLikes()));
        holder.views.setText(String.valueOf(item.getViews()));
        holder.shares.setText(String.valueOf(item.getShares()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView likes,shares,views;
        ViewHolder(View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.image_view);
            likes= itemView.findViewById(R.id.tv_likes);
            views= itemView.findViewById(R.id.tv_views);
            shares= itemView.findViewById(R.id.tv_shares);

        }
    }
}
