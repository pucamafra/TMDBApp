package test.arctouch.tmdbapp.data;


import javax.inject.Inject;

import rx.Observable;
import test.arctouch.tmdbapp.data.remote.TMDbRemoteData;
import test.arctouch.tmdbapp.network.model.MovieResponse;


public class TMDbRepository implements TMDbDataSource {

    private final TMDbRemoteData tmDbRemoteData;

    @Inject
    public TMDbRepository(TMDbRemoteData tmDbRemoteData) {
        this.tmDbRemoteData = tmDbRemoteData;
    }

    @Override
    public Observable<MovieResponse> loadMovies(int page) {
        return this.tmDbRemoteData.loadMovies(page);
    }

    @Override
    public Observable<MovieResponse> searchMovie(String query, int page) {
        return this.tmDbRemoteData.searchMovie(query, page);
    }
}
