package test.arctouch.tmdbapp.network;


import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import test.arctouch.tmdbapp.network.model.MovieResponse;

public interface TMDbApi {

    @GET("/movie/upcoming")
    Observable<MovieResponse> loadMovies(@Query("page") int page);

    @GET("/search/movie")
    Observable<MovieResponse> searchMovie(@Query("query") String query, @Query("page") int page);
}
