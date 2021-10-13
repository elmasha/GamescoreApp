package com.marv.gamescores.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marv.gamescores.R;

public class WebActivity extends AppCompatActivity {
    private FloatingActionButton home,saveNews,shareNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        home = findViewById(R.id.fab_home);
        saveNews = findViewById(R.id.fab_saveOther);
        shareNews = findViewById(R.id.fab_shareOther);
        final String url = getIntent().getStringExtra("url");
        WebView webView = findViewById(R.id.activity_web_wv);
        webView.loadUrl(url);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            }
        });

        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}