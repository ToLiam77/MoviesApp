package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ratings extends AppCompatActivity {
    ListView movieList;
    Button button;
    Button showImageButton;
    TextView ratingText;
    ImageView movieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        movieList = (ListView) findViewById(R.id.RatingsList);
        ratingText = (TextView) findViewById(R.id.ratingText);

        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList = new ArrayList<>();


        //Populate List
        ParseQuery<ParseObject> query = ParseQuery.getQuery("myMovies");
        query.orderByAscending("title");

        try {
            List<ParseObject> MoviesList;

            //MOVIE TITLES
            MoviesList = query.find();
            Log.d("tag", "List: " + MoviesList);

            for (int i = 0; i < MoviesList.size(); i++) {
                ParseObject object = MoviesList.get(i);
                arrayList.add(object.get("title").toString());
            }


        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


        //Display list
        movieList.setChoiceMode(movieList.CHOICE_MODE_SINGLE);

        adapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_single_choice, arrayList);

        movieList.setAdapter(adapter);

        //Retrieve Movie Ratings from IMDB API
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Movie clicked is: " + arrayList.get(position));
                String selectedMovieTitle = arrayList.get(position);

                button = (Button) findViewById(R.id.btn_FindMovieIMDB);
                showImageButton = (Button) findViewById(R.id.btn_ShowImage);

                //Button Click FIND MOVIE IN IMDB
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String IMDB_NAME_API = "https://imdb-api.com/en/API/SearchTitle/k_93u74iak/"
                                + selectedMovieTitle;

                        callVolley(IMDB_NAME_API);
                    }
                });

                //Button Click SHOW IMAGE
                showImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String IMDB_NAME_API = "https://imdb-api.com/en/API/SearchTitle/k_93u74iak/"
                                + selectedMovieTitle;

                        setContentView(R.layout.ratings_imdbmovie);
                        callVolleyImage(IMDB_NAME_API);
                    }
                });
            }
        });



    }

    ///////////////////MOVIE RATINGS VOLLEYS/////////////////////////
    private void callVolley(String url_string){
        RequestQueue queue = Volley.newRequestQueue(this);
        //Receive data from url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_string,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jobject = null;
                        try {
                            jobject = new JSONObject(response);
                            JSONArray array = jobject.getJSONArray("results");
                            JSONObject obj1 = array.getJSONObject(0);
                            System.out.println(obj1.toString());
                            String id = obj1.getString("id");
                            System.out.println("id is " + id);
                            String IMDB_RATING_API = "https://imdb-api.com/en/API/Ratings/k_93u74iak/"
                                    + id;
                            callVolleyMovieRating(IMDB_RATING_API);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ratingText.setText("Movie not found in IMDB");
                            showImageButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    private void callVolleyMovieRating(String url_string){
        RequestQueue queue = Volley.newRequestQueue(this);
        //Receive data from url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_string,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jobject = null;
                        try {
                            jobject = new JSONObject(response);
                            System.out.println(jobject);
                            String rating = jobject.getString("imDb");
                            System.out.println(rating);
                            ratingText.setText("IMDB Rating is " + rating);
                            showImageButton.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }



    ///////////////////MOVIE IMAGES VOLLEYS/////////////////////////
    private void callVolleyImage(String url_string){
        RequestQueue queue = Volley.newRequestQueue(this);
        //Retrieve Image URL from URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_string,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jobject = null;
                        try {
                            jobject = new JSONObject(response);
                            JSONArray array = jobject.getJSONArray("results");
                            JSONObject obj1 = array.getJSONObject(0);
                            System.out.println(obj1.toString());
                            String imgURL = obj1.getString("image");
                            displayImageFromURL(imgURL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ratingText.setText("Movie not found in IMDB");
                            showImageButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);

        //Retrieve Image from Image URL
    }

    private void displayImageFromURL(String url_string){
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(url_string, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                movieImage = (ImageView) findViewById(R.id.movieImage);
                movieImage.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(imageRequest);
    }


}