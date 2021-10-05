package com.marv.gamescores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marv.gamescores.Activities.ViewNewsActivity;
import com.marv.gamescores.Adapters.SliderAdapter;
import com.marv.gamescores.Adapters.StoriesAdapter;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class AllNewsFragment extends Fragment {
View root;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference categoryRef = db.collection("Category");

    private SliderAdapter sliderAdapter;
    private ArrayList<Stories> sliderDataArrayList;
    private SliderView sliderView;
    private String Doc_Id,Doc_Id2;
    private RecyclerView mRecyclerView;
    private StoriesAdapter adapter;
    public AllNewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_all_news, container, false);
       // creating a new array list fr our array list.
        sliderDataArrayList = new ArrayList<>();
        mRecyclerView = root.findViewById(R.id.recycler_stories);

        // initializing our slider view and
        // firebase firestore instance.
        sliderView = root.findViewById(R.id.slider);

        loadImages();
        FetchLatestNews();
       return  root;
    }


    //----Image Slider ----//
    private long likess,commentss;
    private void loadImages() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        newsRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(6)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // inside the on success method we are running a for loop
                // and we are getting the data from Firebase Firestore
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // after we get the data we are passing inside our object class.
                    Stories sliderData = documentSnapshot.toObject(Stories.class);
                    Stories model = new Stories();

                    long mili = sliderData.getTimestamp().getTime();
                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImage(sliderData.getImage());
                    model.setTitle(sliderData.getTitle());
//                    model.setLikesCount(sliderData.getLikesCount());
//                    model.setCommentCount(sliderData.getCommentCount());
//                    model.setCategory(sliderData.getCategory());
//                    model.setViewsCount(sliderData.getViewsCount());
//                    model.setDoc_ID(sliderData.getDoc_ID());
                    model.setTimestamp(sliderData.getTimestamp());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    sliderAdapter = new SliderAdapter(getContext(), sliderDataArrayList);


                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(sliderAdapter);

                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(4);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);


                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///_____ImageSlider end



    //----Fetch LatestNews--
    private void FetchLatestNews() {

//        String UID = mAuth.getCurrentUser().getUid();
        Query query =
                newsRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Stories> transaction = new FirestoreRecyclerOptions.Builder<Stories>()
                .setQuery(query, Stories.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new StoriesAdapter(transaction);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new StoriesAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Stories news = documentSnapshot.toObject(Stories.class);
                String headline = news.getTitle();
                String story = news.getStory();
                String image = news.getImage();
                Doc_Id = news.getDoc_ID();

                if (Doc_Id !=null |headline != null | story != null | image != null){
                    Intent toVendorPref = new Intent(getActivity(), ViewNewsActivity.class);
                    toVendorPref.putExtra("Headline",headline);
                    toVendorPref.putExtra("Story",story);
                    toVendorPref.putExtra("Image",image);
                    toVendorPref.putExtra("doc_ID",Doc_Id);
                    startActivity(toVendorPref);
                    //   viewsCount(Doc_Id);
                }

            }
        });

    }
    //...end fetch..


}