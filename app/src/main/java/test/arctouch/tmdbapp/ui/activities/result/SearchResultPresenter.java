package test.arctouch.tmdbapp.ui.activities.result;


import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.arctouch.tmdbapp.data.TMDbDataSource;
import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.network.model.MovieResponse;
import test.arctouch.tmdbapp.ui.base.BasePresenter;

class SearchResultPresenter extends BasePresenter<SearchResultPresentView> {

    private final TMDbDataSource tmDbDataSource;

    SearchResultPresenter(SearchResultPresentView view, TMDbDataSource tmDbDataSource) {
        this.tmDbDataSource = tmDbDataSource;
        attachView(view);
    }

    void searchMovie(String query) {
        getView().showLoading();
        searchMovie(query, 1, false);
    }

    void searchMovieWithPagination(String query, int page) {
        searchMovie(query, page, true);
    }

    private void searchMovie(String query, int page, boolean loadMore) {
        this.tmDbDataSource.searchMovie(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> handleResult(movieResponse, loadMore),
                        t -> handleError(t, loadMore));
    }

    private void handleResult(MovieResponse movieResponse, boolean loadMore) {
        SearchResultPresentView view = getView();
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

        view.onSearchResultsSuccess(movieList);
        view.dismissLoading();
    }

    private void handleError(Throwable t, boolean loadMore) {
        SearchResultPresentView view = getView();
        if (loadMore) {
            view.onLoadMoreFail(t);
            return;
        }

        view.onSearchMovieFail(t);
        view.dismissLoading();
    }
}
