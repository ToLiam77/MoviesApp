package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity {
    Button save;
    RecyclerView movieList;
    ResultAdapter adapter;
    ArrayList<String> arrayUnchecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        save = findViewById(R.id.btn_Save);
        movieList = findViewById(R.id.favouritesList);


        //DISPLAY CURRENT MOVIES FAVOURITE IN RECYCLERVIEW
        ParseQuery<ParseObject> movieQuery =
                new ParseQuery<ParseObject>("myMovies");
        movieQuery.whereEqualTo("favourite", true);
        movieQuery.orderByAscending("title");

        movieQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objList, ParseException e) {
                if (e == null) {
                    System.out.println("Size: " + objList.size());
                    initData(objList);
                }else{
                    Log.d("ParseQuery", e.getMessage());
                }
            }
        });


        //REMOVE MOVIES FROM FAVOURITE
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayUnchecked = adapter.getListUnchecked();
                //Display elements
                System.out.println(arrayUnchecked);
                for (int i = 0; i < arrayUnchecked.size(); i++){
                    String movieName = arrayUnchecked.get(i);
                    //System.out.println(movieName + " is clicked");
                    updateDataChecked(movieName);
                }

                //Toast and back to Main Menu
                Toast.makeText(getApplicationContext(),
                        "Favourites Updated",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initData(List<ParseObject> objects){
        adapter = new ResultAdapter(this, objects);
        movieList.setLayoutManager(new LinearLayoutManager(this));
        movieList.setAdapter(adapter);

        //SET ALL THE CHECKBOXES AS CHECKED
        adapter.setAllBoxesChecked();
    }

    private void updateDataChecked(String movie_Name){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("myMovies");
        query.whereEqualTo("title", movie_Name);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null){
                    String objectID = object.getObjectId().toString();
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null){
                                object.put("favourite", false);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){
                                            System.out.println(movie_Name + " is removed from favourites");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}