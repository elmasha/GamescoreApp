package com.marv.gamescores.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.marv.gamescores.Fragments.AllNewsFragment;
import com.marv.gamescores.Fragments.OtherNewsFragment;
import com.marv.gamescores.Fragments.PredictionFragment;
import com.marv.gamescores.Fragments.SavedFragment;
import com.marv.gamescores.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private TextView stories,predictions,otherNews;
    private LinearLayout linearLayout;
    private CircleImageView homeProfile;
    private String UserImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference predictionsRef = db.collection("Predictions");
    CollectionReference UsersRef = db.collection("Gamescores_users");



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment SelectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:

                            SelectedFragment = new AllNewsFragment();
                            linearLayout.setVisibility(View.VISIBLE);
                            break;
                        case R.id.navigation_save:

                            SelectedFragment = new SavedFragment();
                            linearLayout.setVisibility(View.GONE);
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
        linearLayout = findViewById(R.id.layout_category);
        homeProfile = findViewById(R.id.home_profileImage);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new
                AllNewsFragment()).commit();

        stories = findViewById(R.id.All_news);
        otherNews = findViewById(R.id.other_news);
        predictions = findViewById(R.id.All_predictions);


        homeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAlerDialog();
            }
        });

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

    private AlertDialog dialog_profile,dialog2;
    private CircleImageView ProfileImage;
    private TextView Username,UserEmail,logOut;
    private String username,userEmail;
    private void ProfileAlerDialog() {

        final  AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_profile, null);
        mbuilder.setView(mView);
        dialog_profile = mbuilder.create();
        dialog_profile.show();
        ProfileImage = mView.findViewById(R.id.ProfileImageView);
        Username = mView.findViewById(R.id.UsernameView);
        UserEmail = mView.findViewById(R.id.EmailView);
        logOut = mView.findViewById(R.id.LogOut);



        if (username != null | userEmail != null){
            Username.setText(username);
            UserEmail.setText(userEmail);
        }

        if (UserImage != null) {
            Picasso.get().load(UserImage).fit().into(ProfileImage);
        }


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser().getUid() != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    dialog2 = builder.create();
                    dialog2.show();
                    builder.setMessage("Are you sure to Log out..\n");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Log_out();

                                }
                            });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog2.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }else {
                    dialog2.dismiss();
                    ToastBack("User account is null");

                }

            }
        });




    }

    void Log_out(){

        String User_ID = mAuth.getCurrentUser().getUid();
        mAuth.signOut();
        Intent logout = new Intent(getApplicationContext(), DashboardActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logout);
        dialog2.dismiss();

        HashMap<String,Object> store = new HashMap<>();
        store.put("device_token", FieldValue.delete());

//        YayaRef.document(User_ID).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//
//                if (task.isSuccessful()){
//
//
//
//                }else {
//
//                    ToastBack( task.getException().getMessage());
//
//                }
//
//            }
//        });

    }



    void LoadDetails(){
        UsersRef.document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return ;
                }

                UserImage = documentSnapshot.getString("UserImage");
                username = documentSnapshot.getString("UserName");
                userEmail = documentSnapshot.getString("UserEmail");
                if (UserImage != null) {
                    Picasso.get().load(UserImage).fit().into(homeProfile);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){

            if (mAuth.getCurrentUser().getUid() != null){
                LoadDetails();
            }else {

            }

        }else {

        }

    }


    private Toast backToast;
    private void ToastBack(String message){


        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        View view = backToast.getView();

        view.getBackground().setColorFilter(Color.parseColor("#F19124"), PorterDuff.Mode.SRC_IN);

        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#168E2A"));
        backToast.show();
    }

    private long backPressedTime;
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
            linearLayout.setVisibility(View.VISIBLE);
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