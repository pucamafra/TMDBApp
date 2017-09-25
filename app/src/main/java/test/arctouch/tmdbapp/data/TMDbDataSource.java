package test.arctouch.tmdbapp.data;

import rx.Observable;
import test.arctouch.tmdbapp.network.model.MovieResponse;

public interface TMDbDataSource {

    Observable<MovieResponse> loadMovies(int page);

    Observable<MovieResponse> searchMovie(String query, int page);

}
