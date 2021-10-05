package com.marv.gamescores.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marv.gamescores.Models.Article;
import com.marv.gamescores.R;
import com.marv.gamescores.utils.OnRecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.ViewHolder> {


    private List<Article> articleArrayList;
    private Context context;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public MainArticleAdapter(List<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }


    @NonNull
    @Override
    public MainArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_article_adapter, viewGroup, false);
        return new MainArticleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainArticleAdapter.ViewHolder viewHolder, int position) {
        final Article articleModel = articleArrayList.get(position);
        if(!TextUtils.isEmpty(articleModel.getTitle())) {
            viewHolder.titleText.setText(articleModel.getTitle());
        }
        if(!TextUtils.isEmpty(articleModel.getDescription())) {
            viewHolder.descriptionText.setText(articleModel.getDescription());
        }
        Picasso.get().load(articleModel.getUrlToImage()).fit().into(viewHolder.article_image);
        viewHolder.artilceAdapterParentLinear.setTag(articleModel.getUrl());
        viewHolder.date.setText(articleModel.getUrl());


    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText,date;
        private ImageView article_image;
        private TextView descriptionText;
        private LinearLayout artilceAdapterParentLinear;
        public ViewHolder(@NonNull View view) {
            super(view);
            titleText = view.findViewById(R.id.article_adapter_tv_title);
            descriptionText = view.findViewById(R.id.article_adapter_tv_description);
            artilceAdapterParentLinear = view.findViewById(R.id.article_adapter_ll_parent);
            article_image = view.findViewById(R.id.article_image);
            date = view.findViewById(R.id.article_date);

            artilceAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onRecyclerViewItemClicked(getAdapterPosition(), view);
                    }
                }
            });

        }
    }




    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }






}

