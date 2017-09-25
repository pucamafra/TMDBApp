package test.arctouch.tmdbapp.ui.activities.result;


import java.util.List;

import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.ui.base.IPresenterView;

interface SearchResultPresentView extends IPresenterView {

    void showLoading();

    void dismissLoading();

    void onSearchResultsSuccess(List<Movie> movieList);

    void onLoadMoreSuccess(List<Movie> movieList);

    void onSearchMovieFail(Throwable t);

    void onLoadMoreFail(Throwable t);

    void enablePagination();

    void disablePagination();

}
