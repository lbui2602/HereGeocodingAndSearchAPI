package com.example.heregeocodingandsearchapi.api;

import com.example.heregeocodingandsearchapi.models.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("geocode")
    Call<ItemResponse> getGeocode(@Query("apikey") String apiKey, @Query("q") String query);
}
