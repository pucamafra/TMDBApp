package test.arctouch.tmdbapp.ui.activities.main;


import java.util.List;

import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.ui.base.IPresenterView;

interface MainPresenterView extends IPresenterView {

    void showLoading();

    void dismissLoading();

    void onLoadMovieSuccess(List<Movie> movieList);

    void enablePagination();

    void onLoadMovieFail(Throwable e);

    void onLoadMoreSuccess(List<Movie> movieList);

    void onLoadMoreFail(Throwable e);

    void disablePagination();

}
