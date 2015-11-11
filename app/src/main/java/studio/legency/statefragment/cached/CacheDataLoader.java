package studio.legency.statefragment.cached;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import studio.legency.statefragment.DataLoadView;
import studio.legency.statefragment.DataLoader;


public abstract class CacheDataLoader<T> implements DataLoader {

    BaseRequest<T> baseRequest;

    DataLoadView<T> loadView;

    private int defaultCacheTime = 1000 * 60 * 5;

    private SpiceManager manager;

    private RequestListener<T> requestListener;

    public CacheDataLoader(SpiceManager manager,
                           final DataLoadView<T> viewDataLoad) {
        this.manager = manager;
        this.loadView = viewDataLoad;
        createListener();
    }

    public CacheDataLoader(Class<T> clazz, SpiceManager manager,
                           DataLoadView<T> viewDataLoad) {
        this(manager, viewDataLoad);
        createRequest(clazz);
    }

    public SpiceManager getManager() {
        return manager;
    }

    public void setManager(SpiceManager manager) {
        this.manager = manager;
    }

    public DataLoadView<T> getV() {
        return loadView;
    }

    public void setV(DataLoadView<T> v) {
        this.loadView = v;
    }

    void createRequest(Class<T> clazz) {
        baseRequest = new BaseRequest<T>(clazz) {

            @Override
            protected T loadDataFrom() throws Exception {
                return loadView.startLoadData();
            }

        };
    }

    void createListener() {
        requestListener = new RequestListener<T>() {

            @Override
            public void onRequestFailure(SpiceException arg0) {
                loadView.loadFailed(arg0);
            }

            @Override
            public void onRequestSuccess(T result) {
                loadView.loaderFinished(result);
            }

        };
    }

    @Override
    public void reload() {
        getData();
    }

    @Override
    public void executeLoad() {
        getData();
    }

    public void getData() {
        manager.execute(baseRequest, getCacheKey(), defaultCacheTime,
                requestListener);

    }

    public int getDefaultCacheTime() {
        return defaultCacheTime;
    }

    public void setDefaultCacheTime(int defaultCacheTime) {
        this.defaultCacheTime = defaultCacheTime;
    }

    abstract Object getCacheKey();

}
