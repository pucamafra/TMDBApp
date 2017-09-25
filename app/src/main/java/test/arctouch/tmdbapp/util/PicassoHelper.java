package test.arctouch.tmdbapp.util;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import test.arctouch.tmdbapp.R;
import test.arctouch.tmdbapp.model.Movie;
import test.arctouch.tmdbapp.util.FormatUtils;

public class PicassoHelper {

    public static void loadMovieImage(Context context, Movie movie, ImageView poster) {
        Picasso.with(context)
                .load(FormatUtils.formatImageUrl(context, movie.backdropPath))
                .fit()
                .into(poster, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(FormatUtils.formatImageUrl(context, movie.posterPath))
                                        .fit()
                                        .placeholder(R.drawable.no_image)
                                        .into(poster);
                            }
                        }
                );
    }

    public static void loadMovieImage(Context context, Movie movie, ImageView image,Callback callback) {
        Picasso.with(context)
                .load(FormatUtils.formatImageUrl(context, movie.backdropPath))
                .fit()
                .centerCrop()
                .into(image, callback);
    }

}
