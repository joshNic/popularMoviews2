package info.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.popularmovies.adapter.MoviesAdapter;
import info.popularmovies.database.DatabaseActivity;

public class MovieFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();
    private String URL = "http://api.themoviedb.org/3/movie/popular?api_key=";


    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private MoviesAdapter mAdapter;
    private ProgressDialog pDialog;
    private ProgressBar pb;


    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        mAdapter = new MoviesAdapter(getActivity(), movieList);
        //        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        pDialog = new ProgressDialog(getActivity());

        pb = view.findViewById(R.id.pbLoading);
        fetchStoreItem(URL);

        return view;
    }

    private void fetchStoreItem(String url) {
        // Showing progress bar before making http request
        pb.setVisibility(ProgressBar.VISIBLE);
        // Creating volley request obj
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pb.setVisibility(ProgressBar.INVISIBLE);

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
                                    movie.setVote_range(movieObj.getDouble("vote_average"));
                                    movie.setId(movieObj.getInt("id"));
                                    movieList.add(movie);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                pb.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_popular:

                fetchStoreItem(URL);
                return true;

            case R.id.rating:

                String URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=";

                fetchStoreItem(URL2);
                return true;
            case R.id.favourites:

                Intent intent = new Intent(getContext(), DatabaseActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}