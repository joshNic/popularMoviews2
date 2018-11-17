package info.popularmovies.database;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import info.popularmovies.Detail;
import info.popularmovies.R;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.MyViewHolder> {
    private Context context;
    private List<DatabaseMovie> movieList;

    public FavouriteListAdapter(Context context, List<DatabaseMovie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public FavouriteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_favorite, parent, false);

        return new FavouriteListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteListAdapter.MyViewHolder holder, final int position) {
        final DatabaseMovie movie = movieList.get(position);
        Glide.with(context)
                .load(movie.getPoster_path())
                .into(holder.thumbnail);

        final String title = movie.getOriginal_title();
        final String poster = movie.getPoster_path();
        final String Oview = movie.getOverview();
        final String date = movie.getRelease_date();
        final Double range = movie.getVote_range();
        final int movieId = movie.getId();

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Detail.class);
                intent.putExtra("original_title", title);
                intent.putExtra("poster_path", poster);
                intent.putExtra("release_date", date);
                intent.putExtra("vote_average", range);
                intent.putExtra("over_view", Oview);
                intent.putExtra("id", movieId);
                view.getContext().startActivity(intent);

            }
        });
    }

    void setWords(List<DatabaseMovie> words) {
        movieList = words;
        notifyDataSetChanged();
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
