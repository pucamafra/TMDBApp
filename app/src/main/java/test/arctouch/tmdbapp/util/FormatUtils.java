package test.arctouch.tmdbapp.util;


import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import test.arctouch.tmdbapp.R;
import test.arctouch.tmdbapp.model.MovieGenre;

public class FormatUtils {

    public static String formatImageUrl(Context context, String image) {
        return context.getString(R.string.image_base_url, image);
    }

    public static String formatGenre(Context context, int[] genreIds) {
        if (genreIds.length == 0)
            return getString(context, R.string.no_genre);

        StringBuilder genre = new StringBuilder(MovieGenre.getById(genreIds[0]).getTitle());
        for (int i = 1; i < genreIds.length; i++) {
            genre.append(", ");
            genre.append(MovieGenre.getById(genreIds[i]).getTitle());
        }
        return genre.toString();
    }

    public static String formatDate(Context context, Date date) {
        if (date == null) {
            return getString(context, R.string.release_date);
        }
        return new SimpleDateFormat("MM/dd/yyy", Locale.getDefault()).format(date);
    }


    private static String getString(Context context, int id) {
        return context.getString(id);
    }
}
