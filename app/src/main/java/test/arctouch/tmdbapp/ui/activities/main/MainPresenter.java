package test.arctouch.tmdbapp.ui.activities.main;


import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.arctouch.tmdbapp.data.TMDbDataSource;
import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.network.model.MovieResponse;
import test.arctouch.tmdbapp.ui.base.BasePresenter;

class MainPresenter extends BasePresenter<MainPresenterView> {

    private final TMDbDataSource tmDbDataSource;

    MainPresenter(MainPresenterView view, TMDbDataSource tmDbDataSource) {
        this.tmDbDataSource = tmDbDataSource;
        attachView(view);
    }

    void loadMovies() {
        getView().showLoading();
        loadMovies(1, false);
    }

    void loadMoviesWithPagination(int page) {
        loadMovies(page, true);
    }

    private void loadMovies(int page, boolean loadMore) {
        this.tmDbDataSource.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> handleResult(movieResponse, loadMore),
                        t -> handleError(t, loadMore));
    }

    private void handleResult(MovieResponse movieResponse, boolean loadMore) {
        MainPresenterView view = getView();
        List<Movie> movieList = movieResponse.movieList;
        int totalPages = movieResponse.totalPages;

        // load more
        if (loadMore) {
            if (movieList.isEmpty()) {
                view.disablePagination();
                return;
            }

            view.onLoadMoreSuccess(movieList);
            return;
        }

        // search
        if (totalPages > 1) {
            view.enablePagination();
        }

        view.onLoadMovieSuccess(movieList);
        view.dismissLoading();
    }

    private void handleError(Throwable t, boolean loadMore) {
        MainPresenterView view = getView();
        if (loadMore) {
            view.onLoadMoreFail(t);
            return;
        }

        view.onLoadMovieFail(t);
        view.dismissLoading();
    }
}
