package info.popularmovies.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AppExecutor {

    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final java.util.concurrent.Executor diskIO;

    public AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

}