package com.example.heregeocodingandsearchapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.heregeocodingandsearchapi.adapters.ItemAdapter;
import com.example.heregeocodingandsearchapi.api.ApiService;
import com.example.heregeocodingandsearchapi.api.RetrofitClient;
import com.example.heregeocodingandsearchapi.models.Item;
import com.example.heregeocodingandsearchapi.models.ItemResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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
    static ItemAdapter itemAdapter;
    SearchView searchView;
    Handler handler;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    int limit=15;
    String at="-13.163068,-72.545128";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        getLocation();
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
                }, 1000);
                return true;
            }
        });
    }
    private void getLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                at=latitude+","+longitude;
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rcvItem = findViewById(R.id.rcv_item);
        searchView = findViewById(R.id.searchview);
        listItem=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvItem.setLayoutManager(linearLayoutManager);
    }

    private void getData(String textSearch) {
        itemAdapter = new ItemAdapter(listItem, this, textSearch);
        rcvItem.setAdapter(itemAdapter);
    }

    private void callApi(String q) {
        ApiService apiService = RetrofitClient.create(ApiService.class);
        apiService.getSearch(apiKey,limit,at, q).enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        itemResponse = new ItemResponse();
                        itemResponse = response.body();
                        if (itemResponse != null) {
                            listItem.clear();
                            listItem.addAll(itemResponse.getItems());
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                recreate();

            } else {
                Toast.makeText(this, "Please grant location permission to use the application.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}