package com.marv.gamescores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    public OtherNewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_other_news, container, false);
        mRecyclerView = root.findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("sports","en",getString(R.string.news_api_key));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if(articleList.size()>0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);



                        mRecyclerView.setAdapter(mainArticleAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });







        return root;
    }


    @Override
    public void onRecyclerViewItemClicked(int position, View view) {
        switch (view.getId()) {
            case R.id.article_date:
                String tag = (String) view.getTag();
                if (!TextUtils.isEmpty(tag)) {
                    Intent webActivity = new Intent(getContext(), WebActivity.class);
                    webActivity.putExtra("url", tag);
                    startActivity(webActivity);
                }
                break;
        }
    }
}