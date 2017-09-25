package test.arctouch.tmdbapp.network.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import test.arctouch.tmdbapp.model.Movie;

public class MovieResponse {

    @SerializedName("results")
    public List<Movie> movieList;

    @SerializedName("total_results")
    int totalResults;

    @SerializedName("total_pages")
    public int totalPages;

    public int page;
}
