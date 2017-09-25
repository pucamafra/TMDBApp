package test.arctouch.tmdbapp.ui.activities.result;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import test.arctouch.tmdbapp.ui.recycleViewItem.MovieItem;
import test.arctouch.tmdbapp.ui.recycleViewItem.ProgressItem;

public class SearchResultActivity extends AppCompatActivity implements SearchResultPresentView, MovieItem.OnClickListener, FlexibleAdapter.EndlessScrollListener {

    private static final String TAG = SearchResultActivity.class.getSimpleName();

    @Inject
    TMDbRepository repository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    String query;

    SearchResultPresenter presenter;

    @BindView(R.id.movie_list)
    RecyclerView movieList;

    private FlexibleAdapter<AbstractFlexibleItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        TMDdApplication.getApiComponent().inject(this);
        setupLayout();
        this.presenter = new SearchResultPresenter(this, this.repository);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.presenter.searchMovie(query);
        }
    }

    private void setupLayout() {
        setSupportActionBar(this.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.adapter = new FlexibleAdapter<>(new ArrayList<AbstractFlexibleItem>());
        this.movieList.setAdapter(this.adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.movieList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        this.movieList.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResultsSuccess(List<Movie> movieList) {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        for (Movie movie : movieList) {
            items.add(new MovieItem(movie, this));
        }
        this.adapter.updateDataSet(items);
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
    public void onSearchMovieFail(Throwable t) {
        Log.d(TAG, "onLoadMoreFail:" + t.getMessage());
        Toast.makeText(this, R.string.load_movies_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreFail(Throwable t) {
        Log.d(TAG, "onLoadMoreFail:" + t.getMessage());
        Toast.makeText(this, R.string.load_movies_error, Toast.LENGTH_SHORT).show();
        this.adapter.onLoadMoreComplete(new ArrayList<>());
    }

    @Override
    public void enablePagination() {
        this.adapter.setEndlessScrollListener(this, new ProgressItem());
        this.adapter.setEndlessPageSize(20);
    }

    @Override
    public void disablePagination() {
        this.adapter.onLoadMoreComplete(null);
    }

    @Override
    public void noMoreLoad(int newItemsSize) {
        Log.d(TAG, "noMoreLoad");
    }

    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        this.presenter.searchMovieWithPagination(this.query, currentPage + 1);
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
    public void noSearchResult() {
        ButterKnife.findById(this, R.id.tv_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Log.d(TAG, "Movie " + movie.title + " was selected");
        startActivity(MovieDetailActivity.createInstance(this, movie));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.detachView();
    }
}
