package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity {
    Button search;
    ListView movieList;
    EditText enterMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = (Button) findViewById(R.id.btnSearch);
        movieList = (ListView) findViewById(R.id.SearchMovieList);
        enterMovie = (EditText) findViewById(R.id.editTextSearch);
    }

    public void clickLookup(View view){
        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList = new ArrayList<>();

        TextView emptySearchMessage = (TextView) findViewById(R.id.emptyResultWarning);
        emptySearchMessage.setVisibility(View.INVISIBLE);

        //Populate List
        String enteredText = enterMovie.getText().toString();

        ParseQuery<ParseObject> queryMovies = ParseQuery.getQuery("myMovies");
        queryMovies.whereMatches("title", enteredText, "i");
        ParseQuery<ParseObject> queryDirector = ParseQuery.getQuery("myMovies");
        queryDirector.whereMatches("director", enteredText, "i");
        ParseQuery<ParseObject> queryActors = ParseQuery.getQuery("myMovies");
        queryActors.whereMatches("actors", enteredText, "i");

        ParseQuery<ParseObject> query =
                ParseQuery.or(Arrays.asList(queryMovies, queryDirector, queryActors));


        try {
            List<ParseObject> MoviesList = query.find();
            Log.d("tag", "List: " + MoviesList);

            for (int i = 0; i < MoviesList.size(); i++) {
                ParseObject object = MoviesList.get(i);
                arrayList.add(object.get("title").toString());
                System.out.println(object.get("title"));
            }

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }




        //Display list
        if (arrayList.isEmpty()){
            emptySearchMessage.setVisibility(View.VISIBLE);
        }

        adapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, arrayList);

        movieList.setAdapter(adapter);
    }

}