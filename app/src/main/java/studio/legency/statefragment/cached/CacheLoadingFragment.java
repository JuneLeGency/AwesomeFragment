package studio.legency.statefragment.cached;

import com.apkfuns.logutils.LogUtils;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.NoNetworkException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import studio.legency.statefragment.BaseLoadingFragment;
import studio.legency.statefragment.DataLoader;

public abstract class CacheLoadingFragment<T> extends BaseLoadingFragment<T> {
    CacheDataLoader<T> loader;

    private SpiceManager spiceManager = new SpiceManager(
            Jackson2SpringAndroidSpiceService.class);

    public CacheLoadingFragment() {
        getTClass();
    }

    /**
     * the class want to get if this is a list please write List V
     *
     * @return
     */
    protected abstract Class<?> requestClass();

    protected abstract Object cacheKey();

    @Override
    public DataLoader initLoader() {
        loader = new CacheDataLoader<T>(spiceManager, this) {

            @Override
            Object getCacheKey() {
                return getActivity().getResources().getConfiguration().locale
                        .getLanguage() + cacheKey();
            }

        };
        Class<?> clazz = requestClass();
        loader.createRequest((Class<T>) clazz);
        return loader;
    }

    /**
     * 类型校验
     */
    private void getTClass() {
        //TODO type checkment
        //((ParameterizedType)(((Class)getClass().getGenericSuperclass()).getGenericSuperclass())).getActualTypeArguments()[0];
        //ParameterizedTypeImpl
        Type genType = ((Class) getClass().getGenericSuperclass()).getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        LogUtils.d("class " + params[0].toString());
    }

    void setCacheTime(int time) {
        loader.setDefaultCacheTime(time);
    }

    @Override
    public void loadFailed(Exception arg0) {
        LogUtils.e(arg0);
        if (arg0 instanceof NoNetworkException) {
            networkError();
        }
        super.loadFailed(arg0);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

}
