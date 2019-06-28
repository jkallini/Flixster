package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // api key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieDetailsActivity";
    // instance fields
    AsyncHttpClient client;

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView (R.id.tvOverview) TextView tvOverview;
    @BindView (R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView (R.id.rbVoteAverage) RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        /*
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        */

        ButterKnife.bind(this);
        // TODO Use fields...

        // unwrap movie passed via the intent using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        // confirm deserialization
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvReleaseDate.setText(String.format("Release date: %s", movie.getReleaseDate()));

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //getVideoTrailer();

    }



    // get the list of currently playing movies from the API
    private void getVideoTrailer() {
        // create the url
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject obj = results.getJSONObject(0);
                    String key = obj.getString("key");

                    Log.i(TAG, "Got the results array");

                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("video_key", key);
                    MovieDetailsActivity.this.startActivity(intent);

                } catch (JSONException e) {
                    logError("Failed to parse JSON array", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    // handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser) {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    // get url movies/movie-id/video
    // int id = movie.getId();
    // Create an endpoint
    // get JSON object
    // get results
    // get string "key"
    // don't notify adapter

    // String key = "xx...."
    // Intent in = new Intent(MDA, MT)
    // in.putExtra("videoId", key)
    // StartActivity(in);

}
