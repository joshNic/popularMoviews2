package info.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.popularmovies.adapter.MoviesAdapter;
import info.popularmovies.database.DatabaseActivity;
import info.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private final String URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private MoviesAdapter mAdapter;

    //private ProgressBar pb;
    public static int index = -1;
    public static int top = -1;
    private GridLayoutManager mLayoutManager;
    private Parcelable listState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieList = new ArrayList<>();
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("f");
            mLayoutManager.onRestoreInstanceState(listState);
            movieList = savedInstanceState.getParcelableArrayList("list");
            //Toast.makeText(this, "instance", Toast.LENGTH_SHORT).show();
        } else {
            //pDialog = new ProgressDialog(this);

            //pb = findViewById(R.id.pbLoading);
            fetchStoreItem(URL);
            //Toast.makeText(this, "New", Toast.LENGTH_SHORT).show();
        }
        mAdapter = new MoviesAdapter(this, movieList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the actionbar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_popular:

                fetchStoreItem(URL);
                return true;

            case R.id.rating:

                fetchStoreItem(URL2);
                return true;
            case R.id.favourites:

                Intent intent = new Intent(this, DatabaseActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("f", listState);
        outState.putSerializable("list", (Serializable) movieList);
    }

    @Override
    public void onPause() {
        super.onPause();
        index = mLayoutManager.findFirstVisibleItemPosition();
        View view = recyclerView.getChildAt(0);
        top = (view == null) ? 0 : (view.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        //set recyclerview position
        if (index != -1) {
            mLayoutManager.scrollToPositionWithOffset(index, top);
        }
    }


    private void fetchStoreItem(String url) {
        // Showing progress bar before making http request
        //pb.setVisibility(ProgressBar.VISIBLE);
        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //pb.setVisibility(ProgressBar.INVISIBLE);
                       // pb.setVisibility(ProgressBar.INVISIBLE);

                        // Parsing json
                        for (int j = 0; j < response.length(); j++) {
                            try {
                                JSONArray movieArray = response.getJSONArray("results");
                                movieList.clear();
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObj = (JSONObject) movieArray.get(i);

                                    Movie movie = new Movie();

                                    movie.setOriginal_title(movieObj.getString("original_title"));
                                    movie.setPoster_path(movieObj.getString("poster_path"));
                                    movie.setOverview(movieObj.getString("overview"));
                                    movie.setRelease_date(movieObj.getString("release_date"));
                                    movie.setVote_average(movieObj.getDouble("vote_average"));
                                    movie.setId(movieObj.getInt("id"));
                                    movieList.add(movie);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //pb.setVisibility(ProgressBar.INVISIBLE);
                            }


                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pb.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(movieReq);
    }
}