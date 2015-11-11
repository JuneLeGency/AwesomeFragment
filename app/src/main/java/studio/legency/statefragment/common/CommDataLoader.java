package studio.legency.statefragment.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import studio.legency.statefragment.DataLoadView;
import studio.legency.statefragment.DataLoader;

/**
 * use Fragment default DataLoader without cache
 *
 * @param <T>
 * @author legency
 */
public abstract class CommDataLoader<T> implements
        LoaderManager.LoaderCallbacks<T>, DataLoader {

    Fragment f;

    DataLoadView<T> viewDataLoad;

    public CommDataLoader(Fragment f, DataLoadView<T> v) {
        super();
        this.f = f;
        this.viewDataLoad = v;
    }

    @Override
    public Loader<T> onCreateLoader(int arg0, Bundle arg1) {
        return new AsyncTaskLoader<T>(f.getActivity()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public T loadInBackground() {
                return viewDataLoad.startLoadData();
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<T> arg0, T arg1) {
        viewDataLoad.loaderFinished(arg1);
    }

    @Override
    public void onLoaderReset(Loader<T> arg0) {
        viewDataLoad.loadReset();
    }

    @Override
    public void reload() {
        f.getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void executeLoad() {
        f.getLoaderManager().initLoader(0, null, this);
    }
}
