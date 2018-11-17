package info.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.popularmovies.R;
import info.popularmovies.Trailer;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder> {
    private Context context;
    private List<Trailer> trailersList;

    public TrailersAdapter(Context context, List<Trailer> trailersList) {
        this.context = context;
        this.trailersList = trailersList;
    }

    @Override
    public TrailersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_card, parent, false);

        return new TrailersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.MyViewHolder holder, final int position) {
        final Trailer trailer = trailersList.get(position);

        final String name = trailer.getName().toString().replaceAll("\"", "");
        final String key = trailer.getKey();
        final String type = trailer.getType();
        holder.nameTv.setText(name);
        holder.typeTv.setText(type);
        holder.thumbnail.setImageResource(R.drawable.ic_play_in_film_strip);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseUrl = "https://www.youtube.com/watch?v="+key;
                Intent webBrowserIntent = new Intent(Intent.ACTION_VIEW);
                webBrowserIntent.setData(Uri.parse(baseUrl));
                view.getContext().startActivity(webBrowserIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, typeTv;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.name);
            typeTv = view.findViewById(R.id.type);
            thumbnail = view.findViewById(R.id.imageView);
            cardView = view.findViewById(R.id.card_view);


        }
    }

}

