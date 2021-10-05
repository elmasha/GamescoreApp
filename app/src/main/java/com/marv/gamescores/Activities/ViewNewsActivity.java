package com.marv.gamescores.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.squareup.picasso.Picasso;

public class ViewNewsActivity extends AppCompatActivity {
    private TextView headline,Story,Story2,Story3,Story4,title,SubTitle,SubHeading,SubHeading2,SubHeading3,SubHeading4,
            likeCount,commentCount,shareCount,viewsCount;
    private ImageView new_image;
    private String Doc_Id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference predictionsRef = db.collection("Predictions");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        Bundle extra = getIntent().getExtras();
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
//        save= findViewById(R.id.fab_save);
//        like = findViewById(R.id.fab_like);
//        comment = findViewById(R.id.fab_comment);
        likeCount = findViewById(R.id.like_view_count);
        commentCount = findViewById(R.id.comment_view_count);
        viewsCount = findViewById(R.id.eye_view_count);
//        recyclerView = findViewById(R.id.commentView_Recycler);
//        adView = (AdView) findViewById(R.id.adView2);

        LoadDetails();
        LoadDetailsPre();
    }

    private String news_imaged, headlines, stories,stories2,stories3,stories4,categories,headings1,headings2,headings3,headings4;
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

                        headlines = mynews.getTitle();
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
//                        likie = mynews.getLikesCount();
//                        commentie = mynews.getCommentCount();
//                        views = mynews.getViewsCount();
                        //story_ID = mynews.getDoc_ID();


                        if (news_imaged != null){
                            Picasso.get().load(news_imaged).fit().into(new_image);

                        }

                        headline.setText(headlines);
                        SubHeading.setText(headings1);
                        SubHeading2.setText(headings2);
                        SubHeading3.setText(headings3);
                        SubHeading4.setText(headings4);
                        Story.setText(stories);
                        Story2.setText(stories2);
                        Story3.setText(stories3);
                        Story4.setText(stories4);
                        Story.setMovementMethod(new ScrollingMovementMethod());
                        title.setText(headlines);
//                        likeCount.setText(likie+"");
//                        commentCount.setText(commentie+"");
//                        viewsCount.setText(views+"");


                    } else {

                    }


                }
            });
        }

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

                        headlines = mynews.getTitle();
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
//                        likie = mynews.getLikesCount();
//                        commentie = mynews.getCommentCount();
//                        views = mynews.getViewsCount();
                        //story_ID = mynews.getDoc_ID();


                        if (news_imaged != null){
                            Picasso.get().load(news_imaged).fit().into(new_image);

                        }

                        headline.setText(headlines);
                        SubHeading.setText(headings1);
                        SubHeading2.setText(headings2);
                        SubHeading3.setText(headings3);
                        SubHeading4.setText(headings4);
                        Story.setText(stories);
                        Story2.setText(stories2);
                        Story3.setText(stories3);
                        Story4.setText(stories4);
                        Story.setMovementMethod(new ScrollingMovementMethod());
                        title.setText(headlines);
//                        likeCount.setText(likie+"");
//                        commentCount.setText(commentie+"");
//                        viewsCount.setText(views+"");


                    } else {

                    }


                }
            });
        }

    }
}