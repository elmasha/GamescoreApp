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
import com.marv.gamescores.Activities.WebActivity;
import com.marv.gamescores.Adapters.PredictionAdapter;
import com.marv.gamescores.Adapters.SavedAdapter;
import com.marv.gamescores.Adapters.SliderAdapter;
import com.marv.gamescores.Adapters.StoriesAdapter;
import com.marv.gamescores.Models.SavedNews;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class SavedFragment extends Fragment {
View root;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsRef = db.collection("Stories");
    CollectionReference categoryRef = db.collection("Category");
    CollectionReference SavedRef = db.collection("Read_later");

    private SliderAdapter sliderAdapter;
    private ArrayList<Stories> sliderDataArrayList;
    private SliderView sliderView;
    private String Doc_Id,Doc_Id2;
    private RecyclerView mRecyclerView;
    private SavedAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchLatestNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_saved, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_saved);
        swipeRefreshLayout = root.findViewById(R.id.swipeSaved);

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

    private String cat;
    private void FetchLatestNews() {

        //        String UID = mAuth.getCurrentUser().getUid();
        Query query =
                SavedRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SavedNews> transaction = new FirestoreRecyclerOptions.Builder<SavedNews>()
                .setQuery(query, SavedNews.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new SavedAdapter(transaction);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new SavedAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SavedNews news = documentSnapshot.toObject(SavedNews.class);
                String url = news.getUrl();
                cat = news.getCategory();
                Doc_Id = documentSnapshot.getId();

                if (cat.equals("story") ){
                    if (Doc_Id != null){
                        Intent toview = new Intent(getActivity(), ViewNewsActivity.class);
                        toview.putExtra("doc_ID",Doc_Id);
                        startActivity(toview);
                    }

                }else if (cat.equals("article") ){
                    if (url != null)
                    {
                        Intent toWeb = new Intent(getActivity(), WebActivity.class);
                        toWeb.putExtra("url",url);
                        startActivity(toWeb);
                    }

                }



            }
        });

    }
}