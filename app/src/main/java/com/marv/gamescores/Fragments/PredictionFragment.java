package com.marv.gamescores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.marv.gamescores.Activities.ViewNewsActivity;
import com.marv.gamescores.Adapters.PredictionAdapter;
import com.marv.gamescores.Adapters.SliderAdapter;
import com.marv.gamescores.Adapters.StoriesAdapter;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class PredictionFragment extends Fragment {
View root;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Predictions");
    CollectionReference categoryRef = db.collection("Category");

    private SliderAdapter sliderAdapter;
    private ArrayList<Stories> sliderDataArrayList;
    private SliderView sliderView;
    private String Doc_Id,Doc_Id2;
    private RecyclerView mRecyclerView;
    private PredictionAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PredictionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_prediction, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_predictions);
        swipeRefreshLayout = root.findViewById(R.id.swipePrediction);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchLatestNews();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        FetchLatestNews();
        return root;
    }



    //----Fetch LatestNews--
    private void FetchLatestNews() {

//        String UID = mAuth.getCurrentUser().getUid();
        Query query =
                newsRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Stories> transaction = new FirestoreRecyclerOptions.Builder<Stories>()
                .setQuery(query, Stories.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new PredictionAdapter(transaction);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new PredictionAdapter.OnItemCickListener() {
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