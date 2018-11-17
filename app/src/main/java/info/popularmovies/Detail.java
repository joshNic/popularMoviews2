package info.popularmovies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.popularmovies.adapter.ReviewsAdapter;
import info.popularmovies.adapter.TrailersAdapter;
import info.popularmovies.database.AppExecutor;

import info.popularmovies.database.DatabaseMovie;

import info.popularmovies.database.MovieRoomDatabase;
import info.popularmovies.database.MovieViewModel;

public class Detail extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private TextView overView, releaseDate, voteRange;
    private ImageView posterPath;
    private List<Review> reviewsList;
    private List<Trailer> trailersList;
    private ReviewsAdapter rAdpter;
    private TrailersAdapter tAdpter;
    private static MovieRoomDatabase db;
    private DatabaseMovie favourite;
    private RecyclerView trailerRecyclerView, reviewsRecyclerView;
    private MovieViewModel mWordViewModel;

    private SharedPreferences sharedPreferences;
    private boolean isFavoriteMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        db = MovieRoomDatabase.getDatabase(getApplicationContext());
        overView = findViewById(R.id.over_view);
        releaseDate = findViewById(R.id.release_date);
        voteRange = findViewById(R.id.vote_average);
        posterPath = findViewById(R.id.poster_path);
        trailerRecyclerView = findViewById(R.id.trailerContent);
        trailersList = new ArrayList<>();
        tAdpter = new TrailersAdapter(this, trailersList);
        //        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        trailerRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        trailerRecyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        //trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setAdapter(tAdpter);

        reviewsRecyclerView = findViewById(R.id.reviewsContent);
        reviewsList = new ArrayList<>();
        rAdpter = new ReviewsAdapter(this, reviewsList);
        RecyclerView.LayoutManager llayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(llayoutManager);

        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(rAdpter);
        final ProgressBar progressBar = findViewById(R.id.progress);

        Intent i = getIntent();
        final String original_title = i.getStringExtra("original_title");
        final String over_view = i.getStringExtra("over_view");
        final String poster_path = i.getStringExtra("poster_path");
        final Double vote_average = i.getDoubleExtra("vote_average", 0.0);
        final String release_date = i.getStringExtra("release_date");
        final int movie_id = i.getIntExtra("id", 0);


        String reviewsUrl = "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?sort_by=popularity.desc&api_key=";
        String trailersUrl = "http://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=";

        overView.setText(over_view);
        releaseDate.setText(release_date);
        voteRange.setText(String.valueOf(vote_average));


        Glide.with(this)
                .load(poster_path)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(posterPath);

        mWordViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        //mMovie = ViewModelProviders.of(this).get(MovieIdViewModel.class);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initFavoriteButton(original_title, over_view, poster_path, vote_average, release_date, movie_id);

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (scrollRange + verticalOffset > 0) {
                    collapsingToolbar.setTitle(original_title);
                    isShow = true;
                }
            }
        });

        fetchReviewItem(reviewsUrl);
        fetchTrailerItem(trailersUrl);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:

                finish();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    private void fetchReviewItem(String url) {

        JsonObjectRequest reviewReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Message", response.toString());
                        //pb.setVisibility(ProgressBar.INVISIBLE);

                        // Parsing json
                        for (int j = 0; j < response.length(); j++) {
                            try {
                                JSONArray reviewArray = response.getJSONArray("results");
                                reviewsList.clear();
                                for (int i = 0; i < reviewArray.length(); i++) {
                                    JSONObject reviewObj = (JSONObject) reviewArray.get(i);

                                    Review reviews = new Review();

                                    reviews.setAuthor(reviewObj.getString("author"));
                                    reviews.setContent(reviewObj.getString("content"));
                                    reviewsList.add(reviews);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        rAdpter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //pb.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(reviewReq);
    }

    private void makeFavourite(final String original_titl, final String release_dat,
                               final String poster_pat, final Double vote_averag,
                               final String over_vie, final int movie_i) {
        DatabaseMovie task = new DatabaseMovie();
        task.setOriginal_title(original_titl);
        task.setId(movie_i);
        task.setOverview(over_vie);
        task.setPoster_path(poster_pat);
        task.setRelease_date(release_dat);
        task.setVote_range(vote_averag);
        mWordViewModel.insert(task);
    }


    private void initFavoriteButton(final String original_titl, final String release_dat,
                                    final String poster_pat, final Double vote_averag,
                                    final String over_vie, final int movie_i) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final DatabaseMovie movieFromDatabase = db.movieDao().getMovieById(movie_i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movieFromDatabase != null) {
                            deleteMovies(original_titl, release_dat, poster_pat, vote_averag, over_vie, movie_i);
                            Toast.makeText(Detail.this, "movie was removed from favourites list", Toast.LENGTH_SHORT).show();
                            isFavoriteMovie = true;

                        } else {
                            makeFavourite(original_titl, release_dat, poster_pat, vote_averag, over_vie, movie_i);
                            Toast.makeText(Detail.this, "movie was added to favourites list", Toast.LENGTH_SHORT).show();
                            isFavoriteMovie = false;

                        }
                    }
                });
            }
        });
    }


    private void deleteMovies(final String original_titl, final String release_dat,
                              final String poster_pat, final Double vote_averag,
                              final String over_vie, final int movie_i) {
        DatabaseMovie task = new DatabaseMovie();
        task.setOriginal_title(original_titl);
        task.setId(movie_i);
        task.setOverview(over_vie);
        task.setPoster_path(poster_pat);
        task.setRelease_date(release_dat);
        task.setVote_range(vote_averag);
        mWordViewModel.delete(task);
    }


    private void fetchTrailerItem(String url) {
        JsonObjectRequest trailerReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Message", response.toString());
                        //pb.setVisibility(ProgressBar.INVISIBLE);

                        // Parsing json
                        for (int j = 0; j < response.length(); j++) {
                            try {
                                JSONArray trailerArray = response.getJSONArray("results");
                                trailersList.clear();
                                for (int i = 0; i < trailerArray.length(); i++) {
                                    JSONObject trailerObj = (JSONObject) trailerArray.get(i);

                                    Trailer trailer = new Trailer();

                                    trailer.setKey(trailerObj.getString("key"));
                                    trailer.setName(trailerObj.getString("name"));
                                    trailer.setType(trailerObj.getString("type"));
                                    trailersList.add(trailer);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        tAdpter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //pb.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(trailerReq);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


}


