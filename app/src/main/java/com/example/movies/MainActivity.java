package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    //CHECK INTERNET CONNECTION
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //MENU BUTTONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickRegisterMovie(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, RegisterMovie.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }
    }

    public void clickDisplayMovies(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, DisplayMovies.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }
    }

    public void clickFavourites(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, Favourites.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }
    }

    public void clickEditMovies(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, EditMovies.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }

    }

    public void clickSearch(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }

    }

    public void clickRatings(View view){
        if (isNetworkConnected()){
            Intent intent = new Intent(this, Ratings.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MessageNoInternet.class);
            startActivity(intent);
        }

    }
}