package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class RegisterMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
    }

    public void onSaveClick(View view){
        //GET VALUES FROM TEXT BOXES
        EditText txtTitle = (EditText) findViewById(R.id.txtBox_Title);
        EditText txtYear = (EditText) findViewById(R.id.txtBox_Year);
        EditText txtDirector = (EditText) findViewById(R.id.txtBox_Director);
        EditText txtActors = (EditText) findViewById(R.id.txtBox_ActorsActresses);
        EditText txtRating = (EditText) findViewById(R.id.txtBox_Rating);
        EditText txtReview = (EditText) findViewById(R.id.txtBox_Review);



        TextView emptyFieldWarning = (TextView) findViewById(R.id.emptyTextWarning);
        emptyFieldWarning.setVisibility(View.INVISIBLE);

        try{
            String Title = txtTitle.getText().toString();
            int Year = Integer.parseInt(txtYear.getText().toString());
            String Director = txtDirector.getText().toString();
            String Actors = txtActors.getText().toString();
            int Rating = Integer.parseInt(txtRating.getText().toString());
            String Review = txtReview.getText().toString();

            //ADD DATA TO DATABASE
            TextView YearWarning = (TextView) findViewById(R.id.yearWarning);
            TextView RatingWarning = (TextView) findViewById(R.id.ratingWarning);

            if(Year < 1895 || Rating < 1 || Rating > 10 ){
                if (Year < 1895){
                    YearWarning.setVisibility(View.VISIBLE);
                }
                if (Rating < 1 || Rating > 10){
                    RatingWarning.setVisibility(View.VISIBLE);
                }
            }else{
                YearWarning.setVisibility(View.INVISIBLE);
                RatingWarning.setVisibility(View.INVISIBLE);

                ParseObject Movie = new ParseObject("myMovies");

                Movie.put("title", Title);
                Movie.put("year", Year);
                Movie.put("director", Director);
                Movie.put("actors", Actors);
                Movie.put("rating", Rating);
                Movie.put("review", Review);

                Movie.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            System.out.println("New Movie Saved");
                        } else {
                            System.out.println("Failed to add data: " + e.getLocalizedMessage());
                        }
                    }
                });

                //Toast and back to Main Menu
                Toast.makeText(getApplicationContext(),
                        "New Movie Registered",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }


        }catch (Exception e){
            emptyFieldWarning.setVisibility(View.VISIBLE);
        }


    }

}