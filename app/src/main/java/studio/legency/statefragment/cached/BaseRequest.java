package studio.legency.statefragment.cached;

import com.octo.android.robospice.request.SpiceRequest;

public abstract class BaseRequest<T> extends SpiceRequest<T> {

    public BaseRequest(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        return loadDataFrom();
    }

    protected abstract T loadDataFrom() throws Exception;
}
