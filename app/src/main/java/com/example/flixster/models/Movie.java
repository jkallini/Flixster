package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // class is Parcelable
public class Movie {

    // values from API, fields must be public for Parceler
    public String title;
    public String overview;
    public String posterPath; // only the path
    public String backdropPath;
    public Double voteAverage; // ratings value

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    // initialize from JSON data,
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
