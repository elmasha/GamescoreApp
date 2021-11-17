package com.marv.gamescores.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marv.gamescores.BuildConfig;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.content.ContentValues.TAG;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class ViewNewsActivity extends AppCompatActivity {
    private TextView headline,Story,Story2,Story3,Story4,title,SubTitle,SubHeading,SubHeading2,SubHeading3,SubHeading4,
            likeCount,commentCount,shareCount,viewsCount;
    private ImageView new_image,imageView2,imageView3,imageView4;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    private String Doc_Id;
    private WebView webInstagram,webTwitter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference predictionsRef = db.collection("Predictions");
    CollectionReference UsersRef = db.collection("Gamescores_users");
    CollectionReference SavedRef = db.collection("Read_later");
    private FloatingActionButton save,share,like;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1001;
    GoogleSignInClient googleSignInClient;
    private Uri ImageUri;

    private UploadTask uploadTask;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressBar progressBar;
    private Bitmap compressedImageBitmap;

    int PERMISSION_ALL = 1000;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    @Override
    protected void onStart() {
        super.onStart();
        LoadDetails();
        LoadDetailsPre();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        mAuth = FirebaseAuth.getInstance();
        Bundle extra = getIntent().getExtras();
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        if (extra != null){ Doc_Id = extra.getString("doc_ID"); }
        headline = findViewById(R.id.view_headline);
        title = findViewById(R.id.view_headlines);
        Story = findViewById(R.id.view_story);
        Story = findViewById(R.id.view_story1);
        Story2 = findViewById(R.id.view_story2);
        Story3 = findViewById(R.id.view_story3);
        Story4 = findViewById(R.id.view_story4);
        SubHeading = findViewById(R.id.sub_heading);
        SubHeading2 = findViewById(R.id.sub_heading2);
        SubHeading3 = findViewById(R.id.sub_heading3);
        SubHeading4 = findViewById(R.id.sub_heading4);
        new_image = findViewById(R.id.view_image);
        save= findViewById(R.id.fab_save);
        share = findViewById(R.id.fab_share);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);
        like = findViewById(R.id.fab_like);
//        comment = findViewById(R.id.fab_comment);
        likeCount = findViewById(R.id.like_view_count);
        commentCount = findViewById(R.id.comment_view_count);
        viewsCount = findViewById(R.id.eye_view_count);
//        recyclerView = findViewById(R.id.commentView_Recycler);
//        adView = (AdView) findViewById(R.id.adView2);
        webInstagram = findViewById(R.id.WebviewInstagram);
        webTwitter = findViewById(R.id.WebviewTwitter);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        LoadDetails();
        LoadDetailsPre();


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareNews();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeCounter();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null){
                        if (mAuth.getCurrentUser().getUid() != null){
                            SaveForLater();
                        }
                }else {
                    SignInDialog();
                }
            }
        });

        SubHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherLinks != null){
                    ToastBack("Please wait launching site..");
                    GoToURL(otherLinks);
                }else {
                    ToastBack("No link attached to this heading");
                }
            }
        });
        SubHeading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherLinks != null){
                    ToastBack("Please wait launching site..");
                    GoToURL(otherLinks);
                }else {
                    ToastBack("No link attached to this heading");
                }
            }
        });
        SubHeading3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (otherLinks != null){
                    ToastBack("Please wait launching site..");
                    GoToURL(otherLinks);
                }else {
                    ToastBack("No link attached to this heading");
                }
            }
        });
        SubHeading4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherLinks != null){
                    ToastBack("Please wait launching site..");
                    GoToURL(otherLinks);
                }else {
                    ToastBack("No link attached to this heading");
                }

            }
        });

        configureGoogleClient();
    }

    private void ShareNews() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gamescore");
        String shareMessage= Title;
        shareMessage = "https://gamescores.co.ke/"+Title + BuildConfig.APPLICATION_ID +"\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share using"));

    }

    private void LikeCounter() {

        if (categories.equals("News")){

            final DocumentReference sfDocRef = db.collection("Stories").document(Doc_Id);
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    // Note: this could be done without a transaction
                    //       by updating the population using FieldValue.increment()
                    double newPopulation = snapshot.getLong("like") + 1;
                    transaction.update(sfDocRef, "like", newPopulation);

                    // Success
                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }else {
                        ToastBack(task.getException().getMessage());
                    }
                }
            });
        }else if (categories.equals("Sponsored")){

            final DocumentReference sfDocRef2 = db.collection("Predictions").document(Doc_Id);
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef2);

                    // Note: this could be done without a transaction
                    //       by updating the population using FieldValue.increment()
                    double newPopulation = snapshot.getLong("like") + 1;
                    transaction.update(sfDocRef2, "like", newPopulation);

                    // Success
                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }else {
                        ToastBack(task.getException().getMessage());
                    }
                }
            });
        }




    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }

            }
        }
        return true;
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

        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //data.getData returns the content URI for the selected Image
                    ImageUri = result.getUri();
                    addImage.setImageURI(ImageUri);
                    break;
            }
    }


    private void showToastMessage(String message) {
        Toast.makeText(ViewNewsActivity.this, message, Toast.LENGTH_LONG).show();
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
                           // SaveUserDetails(UserName,USerNumber,USerEmail,UserPhoto,UserUID);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showToastMessage("Firebase Authentication failed:" + task.getException());
                        }
                    }
                });
    }

    private void SaveUserDetails(String userName,  String uSerEmail, String userPhoto,String userUID) {

        HashMap<String,Object> addUSer = new HashMap<>();
        addUSer.put("UserName",userName);
        addUSer.put("UserImage",userPhoto);
        addUSer.put("UserEmail",uSerEmail);
        addUSer.put("UserUID",userUID);

        UsersRef.document(userUID)
                .set(addUSer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  dialog_signIn.dismiss();
                  progressDialog.dismiss();
              }else {
                  ToastBack(task.getException().getMessage());
              }
            }
        });



    }


    private AlertDialog dialog_signIn;
    private TextInputLayout emailInput,usernameInput,passwordInput;
    private String email,username,password,profileImage;
    private Button BtnConfirm;
    private ImageView addImage;
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
        addImage = mView.findViewById(R.id.Add_image);
        facebook = mView.findViewById(R.id.facebook);



        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(getApplicationContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(ViewNewsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }else {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .setMinCropResultSize(512,512)
                            .setAspectRatio(1,1)
                            .start(ViewNewsActivity.this);
                }
            }
        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToGoogle();
            }
        });



        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser() != null){
                    if (mAuth.getCurrentUser().getUid() != null){

                    }
                }else {

                    EmailPassRegistration();
                }

            }
        });


    }


    ProgressDialog progressDialog;
    private void EmailPassRegistration() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        progressDialog.show();

        if (ImageUri != null){

            File newimage = new File(ImageUri.getPath());

            try {

                Compressor compressor = new Compressor(this);
                compressor.setMaxHeight(200);
                compressor.setMaxWidth(200);
                compressor.setQuality(10);
                compressedImageBitmap = compressor.compressToBitmap(newimage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            final byte[] data = baos.toByteArray();


            final StorageReference ref = storageReference.child("Users/thumbs" + UUID.randomUUID().toString());
            uploadTask = ref.putBytes(data);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    profileImage = downloadUri.toString();

                    email = emailInput.getEditText().getText().toString();
                    password = passwordInput.getEditText().getText().toString();
                    username = usernameInput.getEditText().getText().toString();

                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        SaveUserDetails(username,email,profileImage,mAuth.getCurrentUser().getUid());

                                    }else {
                                        ToastBack(task.getException().getMessage());
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                }
            });

        }else {

            ToastBack("Image not Selected");
        }


    }



    void GoToURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    private String news_imaged, Title,subTitle, stories,stories2,stories3,stories4,
            otherLinks,categories,categories2,headings1,headings2,headings3,headings4,IGLink,twitterLink,story_ID,image2,image3,image4;
    private long likie,commentie,views;
    void LoadDetails() {
        if (Doc_Id !=null){

            newsRef.document(Doc_Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                    @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        Stories mynews = documentSnapshot.toObject(Stories.class);

                        Title = mynews.getTitle();
                        subTitle = mynews.getSubtitle();
                        stories = mynews.getStory();
                        stories2 = mynews.getStory2();
                        stories3 = mynews.getStory3();
                        stories4 = mynews.getStory4();
                        headings1 = mynews.getSubheading1();
                        headings2 = mynews.getSubheading2();
                        headings3 = mynews.getSubheading3();
                        headings4 = mynews.getSubheading4();
                        news_imaged = mynews.getImage();
                        categories= mynews.getCategory();
                        otherLinks = mynews.getOtherLinks();
                        IGLink = mynews.getInstagramLink();
                        twitterLink = mynews.getTwitterLink();
                        image2 = mynews.getImage2();
                        image3 = mynews.getImage3();
                        image4 = mynews.getImage4();
                        story_ID = mynews.getDoc_ID();
                        likie = mynews.getLike();


                        if (image2== null){


                        }else if (image3== null)
                        {

                        }else if (image4==null){


                        }
                            else {
//                            imageView4.setVisibility(View.VISIBLE);
//                            Picasso.get().load(image4).fit().into(imageView4);
//                            imageView3.setVisibility(View.VISIBLE);
//                            Picasso.get().load(image3).fit().into(imageView3);
//                            imageView2.setVisibility(View.VISIBLE);
//                            Picasso.get().load(image2).fit().into(imageView2);
                        }

                        if (IGLink==null){
                            webInstagram.setVisibility(View.GONE);
                        }else if (stories == null){
                            Story.setVisibility(View.GONE);
                        }else if (stories2 == null){
                            Story2.setVisibility(View.GONE);
                        }
                        else if (stories3 == null){
                            Story3.setVisibility(View.GONE);
                        }else if (stories4 == null){
                            Story4.setVisibility(View.GONE);
                        }else if (headings1 == null){
                            SubHeading.setVisibility(View.GONE);
                        }
                        else if (headings2 == null){
                            SubHeading2.setVisibility(View.GONE);
                        }
                        else if (headings3 == null){
                            SubHeading3.setVisibility(View.GONE);
                        }
                        else if (headings4 == null){
                            SubHeading4.setVisibility(View.GONE);
                        }
                        else if (twitterLink==null){
                            webTwitter.setVisibility(View.GONE);
                        }else {
                            addWebViewIG(IGLink);
                            addWebViewTwitter(twitterLink);
                        }
                        likie = mynews.getLike();
                        commentie = mynews.getComment();
                        story_ID = mynews.getDoc_ID();


                        if (news_imaged != null){
                            Picasso.get().load(news_imaged).fit().into(new_image);

                        }

                        headline.setText(Title);
                        SubHeading.setText(headings1);
                        SubHeading2.setText(headings2);
                        SubHeading3.setText(headings3);
                        SubHeading4.setText(headings4);
                        Story.setText(stories);
                        Story2.setText(stories2);
                        Story3.setText(stories3);
                        Story4.setText(stories4);
                        Story.setMovementMethod(new ScrollingMovementMethod());
                        title.setText(Title);
                        likeCount.setText(likie+"");
                        commentCount.setText(commentie+"");
                        viewsCount.setText(views+"");




                    } else {

                    }


                }
            });
        }

    }

    ProgressDialog progressDialog2;
    void SaveForLater(){
        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("please wait...");
        progressDialog2.show();

        HashMap<String,Object> saveNews = new HashMap<>();
        saveNews.put("title",Title);
        saveNews.put("image",news_imaged);
        saveNews.put("description",stories);
        saveNews.put("id",story_ID);
        saveNews.put("url","");
        saveNews.put("category","story");
        saveNews.put("timestamp", FieldValue.serverTimestamp());


        SavedRef.document(story_ID).set(saveNews)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        ToastBack("StorySaved");
                        progressDialog2.dismiss();
                    }else {
                        ToastBack(task.getException().getMessage());
                        progressDialog2.dismiss();
                    }
                    }
                });




    }

    void LoadDetailsPre() {
        if (Doc_Id !=null){

            predictionsRef.document(Doc_Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                    @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        Stories mynews = documentSnapshot.toObject(Stories.class);

                        Title = mynews.getTitle();
                        stories = mynews.getStory();
                        stories2 = mynews.getStory2();
                        stories3 = mynews.getStory3();
                        stories4 = mynews.getStory4();
                        headings1 = mynews.getSubheading1();
                        headings2 = mynews.getSubheading2();
                        headings3 = mynews.getSubheading3();
                        headings4 = mynews.getSubheading4();
                        news_imaged = mynews.getImage();
                        categories= mynews.getCategory();
                        likie = mynews.getLike();
//                        commentie = mynews.getCommentCount();
//                        views = mynews.getViewsCount();
                        //story_ID = mynews.getDoc_ID();


                        if (news_imaged != null){
                            Picasso.get().load(news_imaged).fit().into(new_image);

                        }

                        headline.setText(Title);
                        SubHeading.setText(headings1);
                        SubHeading2.setText(headings2);
                        SubHeading3.setText(headings3);
                        SubHeading4.setText(headings4);
                        Story.setText(stories);
                        Story2.setText(stories2);
                        Story3.setText(stories3);
                        Story4.setText(stories4);
                        Story.setMovementMethod(new ScrollingMovementMethod());
                        title.setText(Title);
                        likeCount.setText(likie+"");
//                        commentCount.setText(commentie+"");
//                        viewsCount.setText(views+"");


                    } else {

                    }


                }
            });
        }

    }


    private void addWebViewTwitter(Object twitterContent){
        if(twitterContent!=null && twitterContent.toString().length()>0){

            webTwitter.setWebChromeClient(new WebChromeClient());
            webTwitter.setWebViewClient(new WebViewClient());
            webTwitter.getSettings().setAppCacheEnabled(true);
            webTwitter.getSettings().setJavaScriptEnabled(true);
            //webView.getSettings().setPluginsEnabled(true);

            webTwitter.loadDataWithBaseURL("https://twitter.com",twitterContent.toString(), "text/html", "utf-8","");


        }
    }


    private void addWebViewIG(Object instagramContent){
        if(instagramContent!=null && instagramContent.toString().length()>0){

            webInstagram.setWebChromeClient(new WebChromeClient());
            webInstagram.setWebViewClient(new WebViewClient());
            webInstagram.getSettings().setAppCacheEnabled(true);
            webInstagram.getSettings().setJavaScriptEnabled(true);
            //webView.getSettings().setPluginsEnabled(true);

            webInstagram.loadDataWithBaseURL("https://instagram.com", instagramContent.toString(), "text/html", "UTF-8", "");


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

}