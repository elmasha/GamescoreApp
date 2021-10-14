package com.marv.gamescores.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.marv.gamescores.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class WebActivity extends AppCompatActivity {
    private FloatingActionButton home,saveNews,shareNews;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference predictionsRef = db.collection("Predictions");
    CollectionReference UsersRef = db.collection("Gamescores_users");
    CollectionReference SavedRef = db.collection("Read_later");
    private FloatingActionButton save,share;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1001;
    GoogleSignInClient googleSignInClient;

    private CircleImageView homeProfile;
    private String UserImage,url,title,image,description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mAuth = FirebaseAuth.getInstance();
        home = findViewById(R.id.fab_home);
        saveNews = findViewById(R.id.fab_saveOther);
        shareNews = findViewById(R.id.fab_shareOther);
        homeProfile = findViewById(R.id.home_profileImage2);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        description =  getIntent().getStringExtra("description");
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
                if (mAuth.getCurrentUser() != null){

                    if (mAuth.getCurrentUser().getUid() !=  null){

                        SaveForLater();
                    }

                }else {
                    SignInDialog();
                }
            }
        });



        configureGoogleClient();

    }

    void LoadDetails(){
        UsersRef.document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return ;
                }

                UserImage = documentSnapshot.getString("UserImage");
                if (UserImage != null) {
                    Picasso.get().load(UserImage).fit().into(homeProfile);
                }

            }
        });
    }


    ProgressDialog progressDialog;
    void SaveForLater(){

         progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait saving...");
        progressDialog.show();

        HashMap<String,Object> saveNews = new HashMap<>();
        saveNews.put("title",title);
        saveNews.put("image",image);
        saveNews.put("description",description);
        saveNews.put("id","");
        saveNews.put("url",url);
        saveNews.put("category","article");
        saveNews.put("timestamp", FieldValue.serverTimestamp());


        SavedRef.document(title).set(saveNews)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ToastBack("StorySaved");
                            progressDialog.dismiss();

                        }else {
                            ToastBack(task.getException().getMessage());
                            progressDialog.dismiss();
                        }
                    }
                });




    }

    private AlertDialog dialog_signIn;
    private TextInputLayout emailInput,usernameInput,passwordInput;
    private String email,username,password;
    private Button BtnConfirm;
    private LinearLayout google,facebook;
    private ProgressBar progressBarMpesa;
    private int phoneState = 0;
    private void SignInDialog(){
        final  AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_signin, null);
        mbuilder.setView(mView);
        dialog_signIn = mbuilder.create();
        dialog_signIn.show();
        emailInput = mView.findViewById(R.id.Email);
        usernameInput = mView.findViewById(R.id.Username);
        passwordInput = mView.findViewById(R.id.Password);
        BtnConfirm = mView.findViewById(R.id.Btn_login);
        google = mView.findViewById(R.id.google);
        facebook = mView.findViewById(R.id.facebook);


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToGoogle();
            }
        });



        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser().getUid() != null){

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

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.

    }


    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showToastMessage("Google Sign in Succeeded");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                showToastMessage("Google Sign in Failed " + e);
            }
        }
    }


    private void showToastMessage(String message) {
        Toast.makeText(WebActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private String UserName,UserPhoto,USerEmail,UserUID,USerNumber;
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            dialog_signIn.dismiss();
                            UserName = user.getDisplayName();
                            USerNumber = user.getPhoneNumber();
                            USerEmail = user.getEmail();
                            UserPhoto = String.valueOf(user.getPhotoUrl());
                            UserUID = user.getUid();
                            SaveUserDetails(UserName,USerNumber,USerEmail,UserPhoto,UserUID);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showToastMessage("Firebase Authentication failed:" + task.getException());
                        }
                    }
                });
    }

    private void SaveUserDetails(String userName, String uSerNumber, String uSerEmail, String userPhoto,String userUID) {

        HashMap<String,Object> addUSer = new HashMap<>();
        addUSer.put("UserName",userName);
        addUSer.put("UserImage",userPhoto);
        addUSer.put("UserNumber",uSerNumber);
        addUSer.put("UserEmail",uSerEmail);
        addUSer.put("UserUID",userUID);

        UsersRef.document(userUID)
                .set(addUSer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog_signIn.dismiss();
                }else {
                    ToastBack(task.getException().getMessage());
                }
            }
        });



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


}