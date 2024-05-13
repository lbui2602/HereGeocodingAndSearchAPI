package com.example.heregeocodingandsearchapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heregeocodingandsearchapi.adapters.ItemAdapter;
import com.example.heregeocodingandsearchapi.api.ApiService;
import com.example.heregeocodingandsearchapi.api.RetrofitClient;
import com.example.heregeocodingandsearchapi.models.Item;
import com.example.heregeocodingandsearchapi.models.ItemResponse;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IClickItem {
    String apiKey = "niP94TCXxmP1HCzvoHTj_w-c0b_iGkLCTAfcdw6zEuc";
    String q = null;
    ItemResponse itemResponse;
    List<Item> listItem;
    RecyclerView rcvItem;
    ItemAdapter itemAdapter;
    SearchView searchView;
    Handler handler;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handler = new Handler();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callApi(newText);
                    }
                }, 100);
                return true;
            }
        });

    }

    private void initView() {
        rcvItem = findViewById(R.id.rcv_item);
        searchView = findViewById(R.id.searchview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvItem.setLayoutManager(linearLayoutManager);
    }

    private void getData(String textSearch) {
        itemAdapter = new ItemAdapter(listItem, this, textSearch);
        rcvItem.setAdapter(itemAdapter);
    }

    private void callApi(String q) {
        ApiService apiService = RetrofitClient.create(ApiService.class);
        apiService.getGeocode(apiKey, q).enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        itemResponse = new ItemResponse();
                        itemResponse = response.body();

                        listItem = new ArrayList<>();
                        if (itemResponse != null) {
                            listItem.addAll(itemResponse.getItems());
                            Log.d("TAG", "onResponse: " + listItem.size());
                            getData(q);
                        }
                    }
                } else {
                    Log.d("TAG", "onResponse: not");
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

    @Override
    public void onClickItem(Item item) {
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=+" + item.getPosition().getLat() + "," + item.getPosition().getLng());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Please install Google Map", Toast.LENGTH_SHORT).show();
        }
    }
}