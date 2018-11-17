package info.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import info.popularmovies.Detail;
import info.popularmovies.Movie;
import info.popularmovies.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private Context context;
    private List<Movie> movieList;

    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_card, parent, false);

        return new MoviesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185" + movie.getPoster_path())
                .into(holder.thumbnail);

        final String title = movie.getOriginal_title();
        final String poster = "https://image.tmdb.org/t/p/w500" + movie.getPoster_path();
        final String Oview = movie.getOverview();
        final String date = movie.getRelease_date();
        final Double range = movie.getVote_range();
        final int movieId = movie.getId();
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.onCardSelected(position, holder.thumbnail);
                Intent intent = new Intent(view.getContext(), Detail.class);
                intent.putExtra("original_title", title);
                intent.putExtra("poster_path", poster);
                intent.putExtra("release_date", date);
                intent.putExtra("vote_average", range);
                intent.putExtra("over_view", Oview);
                intent.putExtra("id", movieId);
                view.getContext().startActivity(intent);
                //Toast.makeText(getActivity(), "Image still downloading.....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            cardView = view.findViewById(R.id.card_view);


        }
    }
}
