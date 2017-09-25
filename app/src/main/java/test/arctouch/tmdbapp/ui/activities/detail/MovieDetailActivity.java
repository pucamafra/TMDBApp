package test.arctouch.tmdbapp.ui.activities.detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.picasso.Callback;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.arctouch.tmdbapp.R;
import test.arctouch.tmdbapp.util.ColorUtil;
import test.arctouch.tmdbapp.util.FormatUtils;
import test.arctouch.tmdbapp.util.PicassoHelper;
import test.arctouch.tmdbapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.poster)
    AppCompatImageView poster;

    @BindView(R.id.overview)
    JustifiedTextView overview;

    @BindView(R.id.release_date)
    AppCompatTextView releaseDate;

    @BindView(R.id.genres)
    AppCompatTextView genres;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    public static Intent createInstance(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(Movie.class.getSimpleName(), movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setTranslucentStatusBar(getWindow());
        setupLayout();
        setupMovie(intent.getParcelableExtra(Movie.class.getSimpleName()));
    }

    private void setupLayout() {
        setSupportActionBar(this.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupMovie(Movie movie) {
        this.collapsingToolbarLayout.setTitle(movie.title);
        this.releaseDate.setText(FormatUtils.formatDate(this, movie.releaseDate));
        this.genres.setText(FormatUtils.formatGenre(this, movie.genreIds));
        this.overview.setText(movie.overview);
        PicassoHelper.loadMovieImage(this, movie, this.poster, new Callback() {
            @Override
            public void onSuccess() {
                dynamicToolbarColor();
            }

            @Override
            public void onError() {
            }
        });
    }

    private void dynamicToolbarColor() {
        Bitmap bitmap = ((BitmapDrawable) this.poster.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(palette -> {
            int mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            int mutedDarkColor = ColorUtil.manipulateColor(mutedColor, 0.8f);
            this.collapsingToolbarLayout.setContentScrimColor(mutedColor);
            this.collapsingToolbarLayout.setStatusBarScrimColor(mutedDarkColor);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;

        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
            return;
        }

        if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}