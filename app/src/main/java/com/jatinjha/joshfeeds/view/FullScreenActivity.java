package com.jatinjha.joshfeeds.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jatinjha.joshfeeds.R;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends BaseAnimationActivity {

    String imageUrl;

    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ivImage = findViewById(R.id.ivImage);
        imageUrl = getIntent().getStringExtra("image");

        Picasso.with(this)
                .load(imageUrl)
                .into(ivImage);

    }
}
