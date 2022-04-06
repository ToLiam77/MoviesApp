package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.List;

public class EditMovies extends AppCompatActivity {
    ListView movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        movieList = (ListView) findViewById(R.id.EditMoviesList);

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
        adapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, arrayList);

        movieList.setAdapter(adapter);


        //Handling Click Events in ListView
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Movie clicked is: " + arrayList.get(position));
                EditSelectedMovie(arrayList.get(position));
            }
        });
    }

    public void EditSelectedMovie(String movieTitle){
        setContentView(R.layout.edit_movies_editmovie);

        EditText edit_MovieTitle = (EditText) findViewById(R.id.editTextMovieName);
        EditText edit_MovieYear = (EditText) findViewById(R.id.editTextMovieYear);
        EditText edit_MovieDirector = (EditText) findViewById(R.id.editTextMovieDirector);
        EditText edit_MovieActors = (EditText) findViewById(R.id.editTextMovieActors);
        EditText edit_MovieReview = (EditText) findViewById(R.id.editTextMovieReview);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.movieRatingStars);
        Switch isFavourite = (Switch) findViewById(R.id.switch_isFavourite);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("myMovies");
        query.whereEqualTo("title", movieTitle);
        ParseObject selectedMovie = new ParseObject("myMovies");
        try{
            selectedMovie = query.getFirst();
        } catch (Exception e){

        }

        edit_MovieTitle.setText(movieTitle);
        edit_MovieYear.setText("" + selectedMovie.getInt("year"));
        int db_year = selectedMovie.getInt("year");
        edit_MovieDirector.setText(selectedMovie.getString("director"));
        String db_director = selectedMovie.getString("director");
        edit_MovieActors.setText(selectedMovie.getString("actors"));
        String db_actors = selectedMovie.getString("actors");
        edit_MovieReview.setText(selectedMovie.getString("review"));
        String db_review = selectedMovie.getString("review");
        ratingBar.setRating(selectedMovie.getInt("rating"));
        int db_rating = selectedMovie.getInt("rating");
        isFavourite.setChecked(selectedMovie.getBoolean("favourite"));
        Boolean db_favourite = selectedMovie.getBoolean("favourite");

        //UPDATE MOVIE
        Button updateMovie = (Button) findViewById(R.id.btnUpdate);
        String movieID = selectedMovie.getObjectId();

        updateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MOVIE TITLE
                if (!edit_MovieTitle.getText().toString().equals(movieTitle)){
                    updateData(movieID, "title", edit_MovieTitle.getText().toString());
                    System.out.println(edit_MovieTitle.getText().toString());
                }
                //MOVIE YEAR
                if (Integer.parseInt(edit_MovieYear.getText().toString()) != db_year){
                    updateData(movieID, "year", edit_MovieYear.getText().toString());
                    System.out.println(Integer.parseInt(edit_MovieYear.getText().toString()));
                }
                //MOVIE DIRECTOR
                if (!edit_MovieDirector.getText().toString().equals(db_director)){
                    updateData(movieID, "director", edit_MovieDirector.getText().toString());
                }
                //MOVIE ACTORS
                if (!edit_MovieActors.getText().toString().equals(db_actors)){
                    updateData(movieID, "actors", edit_MovieActors.getText().toString());
                }
                //MOVIE REVIEW
                if (!edit_MovieReview.getText().toString().equals(db_review)){
                    updateData(movieID, "review", edit_MovieReview.getText().toString());
                }
                //MOVIE SCORE
                if (ratingBar.getRating() != db_rating){
                    updateData(movieID, "rating", "" + ratingBar.getRating());
                }

                //MOVIE FAVOURITE STATUS
                if (isFavourite.isChecked() != db_favourite){
                    updateData(movieID, "favourite", "" + isFavourite.isChecked());
                }



                //Toast and back to Main Menu
                Toast.makeText(getApplicationContext(),
                        "Movie data updated" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void updateData(String movieID, String fieldToUpdate, String updateData){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("myMovies");
        try{
            query.get(movieID);
        }catch (Exception e){
            System.out.println(e);
        }

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null){
                    String objectID = object.getObjectId().toString();
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null){
                                if (fieldToUpdate == "year"){
                                    object.put(fieldToUpdate, Integer.parseInt(updateData));
                                }else if (fieldToUpdate == "rating"){
                                    int a = Math.round(Float.parseFloat(updateData));
                                    object.put(fieldToUpdate, a);
                                }else if (fieldToUpdate == "favourite") {
                                    Boolean newFavStatus = null;
                                    if (updateData.equals("true")){
                                        newFavStatus = true;
                                    }else if (updateData.equals("false")){
                                        newFavStatus = false;
                                    }
                                    object.put(fieldToUpdate, newFavStatus);
                                }else{
                                        object.put(fieldToUpdate, updateData);
                                }
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){
                                            System.out.println(fieldToUpdate +
                                                    " has been replaced with: " + updateData);
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