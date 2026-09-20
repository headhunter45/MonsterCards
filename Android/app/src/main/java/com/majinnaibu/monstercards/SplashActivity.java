package com.majinnaibu.monstercards;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                if (getIntent().getAction() != null) {
                    intent.setAction(getIntent().getAction());
                }
                if (getIntent().getData() != null) {
                    intent.setData(getIntent().getData());
                }
            }
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 1200);
    }
}
