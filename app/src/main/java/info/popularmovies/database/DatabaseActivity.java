package info.popularmovies.database;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import info.popularmovies.R;

public class DatabaseActivity extends AppCompatActivity {
    private MovieViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final FavouriteListAdapter adapter = new FavouriteListAdapter(this, new ArrayList<DatabaseMovie>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mWordViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<List<DatabaseMovie>>() {
            @Override
            public void onChanged(@Nullable final List<DatabaseMovie> movie) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(movie);
            }
        });
    }

}
