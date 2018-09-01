package com.jatinjha.joshfeeds.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

public class SplashScreen extends BaseAnimationActivity {


    Handler handler;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,FeedActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
