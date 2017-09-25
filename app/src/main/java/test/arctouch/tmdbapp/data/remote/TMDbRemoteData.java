package test.arctouch.tmdbapp.data.remote;


import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import test.arctouch.tmdbapp.network.TMDbApi;
import test.arctouch.tmdbapp.network.model.MovieResponse;

@Singleton
public class TMDbRemoteData {

    private final TMDbApi tmDbApi;

    @Inject
    public TMDbRemoteData(TMDbApi tmDbApi) {
        this.tmDbApi = tmDbApi;
    }

    public Observable<MovieResponse> loadMovies(int page) {
        return tmDbApi.loadMovies(page);
    }

    public Observable<MovieResponse> searchMovie(String query, int page) {
        return tmDbApi.searchMovie(query, page);
    }
}
