package info.popularmovies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.popularmovies.R;
import info.popularmovies.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context context;
    private List<Review> reviewsList;

    public ReviewsAdapter(Context context, List<Review> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_card, parent, false);

        return new ReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, final int position) {
        final Review review = reviewsList.get(position);

        final String author = review.getAuthor();
        final String content = review.getContent();
        holder.authorTv.setText("Reviewer "+author);
        holder.contentTv.setText(content);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTv, contentTv;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            authorTv = view.findViewById(R.id.author);
            contentTv = view.findViewById(R.id.content);
            cardView = view.findViewById(R.id.card_view);
            authorTv.setTextColor(Color.RED);

        }
    }

}

