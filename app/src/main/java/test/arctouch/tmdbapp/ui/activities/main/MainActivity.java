package test.arctouch.tmdbapp.ui.activities.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import test.arctouch.tmdbapp.R;
import test.arctouch.tmdbapp.TMDdApplication;
import test.arctouch.tmdbapp.data.TMDbRepository;
import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.ui.activities.detail.MovieDetailActivity;
import test.arctouch.tmdbapp.ui.activities.result.SearchResultActivity;
import test.arctouch.tmdbapp.ui.recycleViewItem.MovieItem;
import test.arctouch.tmdbapp.ui.recycleViewItem.ProgressItem;

public class MainActivity extends AppCompatActivity implements MainPresenterView, MovieItem.OnClickListener, FlexibleAdapter.EndlessScrollListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter presenter;

    @Inject
    TMDbRepository repository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.movie_list)
    RecyclerView movieList;

    private FlexibleAdapter<AbstractFlexibleItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        TMDdApplication.getApiComponent().inject(this);
        setupLayout();
        this.presenter = new MainPresenter(this, this.repository);
        this.presenter.loadMovies();
    }

    private void setupLayout() {
        setSupportActionBar(this.toolbar);
        this.adapter = new FlexibleAdapter<>(new ArrayList<AbstractFlexibleItem>());
        this.movieList.setAdapter(this.adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.movieList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        this.movieList.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultActivity.class)));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchView.clearFocus();
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onLoadMovieSuccess(List<Movie> movieList) {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        for (Movie movie : movieList) {
            items.add(new MovieItem(movie, this));
        }
        this.adapter.updateDataSet(items);
    }

    @Override
    public void onLoadMovieFail(Throwable e) {
        Log.d(TAG, "onLoadMoreFail:" + e.getMessage());
        Toast.makeText(this, R.string.load_movies_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        this.presenter.loadMoviesWithPagination(currentPage + 1);
    }

    @Override
    public void onLoadMoreSuccess(List<Movie> movieList) {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        for (Movie movie : movieList) {
            items.add(new MovieItem(movie, this));
        }
        this.adapter.onLoadMoreComplete(items);
    }

    @Override
    public void onLoadMoreFail(Throwable e) {
        Log.d(TAG, "onLoadMoreFail:" + e.getMessage());
        Toast.makeText(this, R.string.load_movies_error, Toast.LENGTH_SHORT).show();
        this.adapter.onLoadMoreComplete(new ArrayList<>());
    }

    @Override
    public void noMoreLoad(int newItemsSize) {
        Log.d(TAG, "noMoreLoad");
    }

    @Override
    public void disablePagination() {
        this.adapter.onLoadMoreComplete(null);
    }

    @Override
    public void enablePagination() {
        this.adapter.setEndlessScrollListener(this, new ProgressItem());
        this.adapter.setEndlessPageSize(20);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Log.d(TAG, "Movie " + movie.title + " was selected");
        startActivity(MovieDetailActivity.createInstance(this, movie));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void showLoading() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.detachView();
    }
}
