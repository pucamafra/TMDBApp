package test.arctouch.tmdbapp.ui.recycleViewItem;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lid.lib.LabelImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import test.arctouch.tmdbapp.R;
import test.arctouch.tmdbapp.util.FormatUtils;
import test.arctouch.tmdbapp.util.PicassoHelper;
import test.arctouch.tmdbapp.model.Movie;

public class MovieItem extends AbstractFlexibleItem<MovieItem.ViewHolder> {

    public interface OnClickListener {
        void onMovieSelected(Movie movie);
    }

    private final Movie movie;
    private final OnClickListener listener;

    public MovieItem(Movie movie, OnClickListener listener) {
        this.movie = movie;
        this.listener = listener;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MovieItem) {
            MovieItem inItem = (MovieItem) inObject;
            return this.movie.id == inItem.movie.id;
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.movie_item;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ViewHolder viewHolder, int position, List payloads) {
        Context context = viewHolder.itemView.getContext();

        viewHolder.title.setText(movie.title);
        viewHolder.genres.setText(FormatUtils.formatGenre(context, this.movie.genreIds));
        viewHolder.poster.setLabelText(FormatUtils.formatDate(context, this.movie.releaseDate));

        PicassoHelper.loadMovieImage(context, movie, viewHolder.poster);

        viewHolder.view.setOnClickListener(v -> this.listener.onMovieSelected(this.movie));
    }

    static class ViewHolder extends FlexibleViewHolder {
        @BindView(R.id.title)
        AppCompatTextView title;

        @BindView(R.id.genres)
        AppCompatTextView genres;

        @BindView(R.id.poster)
        LabelImageView poster;

        View view;

        ViewHolder(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


}