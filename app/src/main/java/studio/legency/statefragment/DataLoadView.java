package studio.legency.statefragment;

/**
 * @param <T>
 * @author legency
 */
public interface DataLoadView<T> {

    /**
     * start load data
     *
     * @return
     */
    T startLoadData();

    /**
     * data loaded
     *
     * @param result
     */
    void loaderFinished(T result);

    /**
     * data load failed only use full when using cache DataLoader
     *
     * @param arg0
     */
    void loadFailed(Exception arg0);

    /**
     * use android default loader to reset loader  such as when adapter setData null
     */
    void loadReset();

}
