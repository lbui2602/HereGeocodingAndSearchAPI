package com.example.heregeocodingandsearchapi.api;

import com.example.heregeocodingandsearchapi.models.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("autosuggest")
    Call<ItemResponse> getSearch(@Query("apikey") String apiKey,@Query("limit") int limit,@Query("at") String location, @Query("q") String query);
}
