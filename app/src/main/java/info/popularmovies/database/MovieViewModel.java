package info.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MoviesRepository mRepository;

    private LiveData<List<DatabaseMovie>> mAllWords;
    private LiveData<DatabaseMovie> favouriteMovie;
    //Movie id
    private int mId;

    public MovieViewModel(Application application) {
        super(application);
        mRepository = new MoviesRepository(application);
        //favouriteMovie = mRepository.getFavouriteMovieById(mId);
        mAllWords = mRepository.getAllWords();

    }

    LiveData<List<DatabaseMovie>> getAllWords() {
        return mAllWords;
    }


    public void insert(DatabaseMovie word) {
        mRepository.insert(word);
    }

    public void delete(DatabaseMovie word) {
        mRepository.delete(word);
    }

}
