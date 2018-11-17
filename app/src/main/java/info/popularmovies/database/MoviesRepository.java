package info.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MoviesRepository {

    private MovieDao mWordDao;
    private LiveData<List<DatabaseMovie>> mAllWords;
    private static MovieRoomDatabase db;
    private LiveData<List<DatabaseMovie>> mAllWord;



    MoviesRepository(Application application) {
        db = MovieRoomDatabase.getDatabase(application);
        mWordDao = db.movieDao();
        mAllWords = mWordDao.getAllWords();
        //mAllWord = mWordDao.getMovieById(id);
    }

    LiveData<List<DatabaseMovie>> getAllWords() {
        return mAllWords;
    }



    public void insert(DatabaseMovie word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<DatabaseMovie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DatabaseMovie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //Delete

    public void delete(DatabaseMovie word) {
        new deleteAsyncTask(mWordDao).execute(word);
    }

    private static class deleteAsyncTask extends AsyncTask<DatabaseMovie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DatabaseMovie... params) {
            mAsyncTaskDao.deleteBorrow(params[0]);
            return null;
        }
    }
}
