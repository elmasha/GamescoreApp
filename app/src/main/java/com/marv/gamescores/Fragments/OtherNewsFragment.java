package com.marv.gamescores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marv.gamescores.Activities.WebActivity;
import com.marv.gamescores.Adapters.MainArticleAdapter;
import com.marv.gamescores.Models.Article;
import com.marv.gamescores.Models.ResponseModel;
import com.marv.gamescores.R;
import com.marv.gamescores.Rest.ApiClient;
import com.marv.gamescores.Rest.ApiInterface;
import com.marv.gamescores.utils.OnRecyclerViewItemClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OtherNewsFragment extends Fragment implements OnRecyclerViewItemClickListener {
View root;

    private RecyclerView mRecyclerView;
    MainArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public OtherNewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_other_news, container, false);
        mRecyclerView = root.findViewById(R.id.activity_main_rv);
        swipeRefreshLayout = root.findViewById(R.id.swipeOtherNews);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadNews();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));



        LoadNews();


        return root;
    }

    private void LoadNews() {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("sports","en",getString(R.string.news_api_key));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if(articleList.size()>0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onRecyclerViewItemClicked(int position, View view) {
                                switch (view.getId()) {
                                    case R.id.article_adapter_ll_parent:
                                        Article article = (Article) view.getTag();
                                        if(!TextUtils.isEmpty(article.getUrl())) {
                                            Intent webActivity = new Intent(getContext(), WebActivity.class);
                                            webActivity.putExtra("url", article.getUrl());
                                            webActivity.putExtra("title", article.getTitle());
                                            webActivity.putExtra("image", article.getUrlToImage());
                                            webActivity.putExtra("description", article.getDescription());
                                            startActivity(webActivity);
                                        }
                                        break;
                                }
                            }
                        });

                        mRecyclerView.setAdapter(mainArticleAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        LoadNews();
    }

    @Override
    public void onRecyclerViewItemClicked(int position, View view) {
        switch (view.getId()) {
            case R.id.article_date:
                Article article = (Article) view.getTag();
                if(!TextUtils.isEmpty(article.getUrl())) {
                    Intent webActivity = new Intent(getContext(), WebActivity.class);
                    webActivity.putExtra("url", article.getUrl());
                    startActivity(webActivity);
                }
                break;
        }
    }
}