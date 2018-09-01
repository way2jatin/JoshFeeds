package com.jatinjha.joshfeeds.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.jatinjha.joshfeeds.R;


public class BaseAnimationActivity extends AppCompatActivity {
    @Override
    public void finish() {
        super.finish();
        onLeaveThisActivity();
    }

    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartNewActivity();
    }

    protected void onStartNewActivity() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
}

