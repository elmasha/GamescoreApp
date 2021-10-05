package com.marv.gamescores.Rest;

import com.marv.gamescores.Models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("category") String category,@Query("language") String language, @Query("apiKey") String apiKey);
}
