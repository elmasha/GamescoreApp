package com.marv.gamescores.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.marv.gamescores.Fragments.AllNewsFragment;
import com.marv.gamescores.Fragments.OtherNewsFragment;
import com.marv.gamescores.Fragments.PredictionFragment;
import com.marv.gamescores.Fragments.SavedFragment;
import com.marv.gamescores.R;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private TextView stories,predictions,otherNews;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment SelectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:

                            SelectedFragment = new AllNewsFragment();
                            break;
                        case R.id.navigation_save:

                            SelectedFragment = new SavedFragment();
                            break;


                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            SelectedFragment).commit();


                    return true;
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new
                AllNewsFragment()).commit();

        stories = findViewById(R.id.All_news);
        otherNews = findViewById(R.id.other_news);
        predictions = findViewById(R.id.All_predictions);

        stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stories.setTextColor(getResources().getColor(R.color.secondary));
                predictions.setTextColor(getResources().getColor(R.color.primary));
                otherNews.setTextColor(getResources().getColor(R.color.primary));

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new
                        AllNewsFragment()).commit();

            }
        });

        otherNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otherNews.setTextColor(getResources().getColor(R.color.secondary));
                predictions.setTextColor(getResources().getColor(R.color.primary));
                stories.setTextColor(getResources().getColor(R.color.primary));

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new
                        OtherNewsFragment()).commit();

            }
        });

        predictions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stories.setTextColor(getResources().getColor(R.color.primary));
                predictions.setTextColor(getResources().getColor(R.color.secondary));
                otherNews.setTextColor(getResources().getColor(R.color.primary));
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new
                        PredictionFragment()).commit();
            }
        });


    }

    private long backPressedTime;
    private Toast backToast;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2500 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();
            finish();
            return;
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new
                    AllNewsFragment()).commit();
//            layoutCategory.setVisibility(View.VISIBLE);
//            mInterstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    showInterstitial();
//                }
//            });
            stories.setTextColor(getResources().getColor(R.color.primary));
            predictions.setTextColor(getResources().getColor(R.color.primary));
            otherNews.setTextColor(getResources().getColor(R.color.primary));
            backToast = Toast.makeText(getBaseContext(), "Double tap to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

}