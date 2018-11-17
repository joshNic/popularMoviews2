package info.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    @Insert(onConflict = REPLACE)
    void insert(DatabaseMovie movie);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Query("SELECT * from movie_table")
    LiveData<List<DatabaseMovie>> getAllWords();

    @Query("select * from movie_table where id = :id")
    DatabaseMovie getMovieById(int id);

    @Delete
    void deleteBorrow(DatabaseMovie databaseMovies);
}
