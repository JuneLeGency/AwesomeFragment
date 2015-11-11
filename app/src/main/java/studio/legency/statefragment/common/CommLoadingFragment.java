package studio.legency.statefragment.common;

import studio.legency.statefragment.BaseLoadingFragment;
import studio.legency.statefragment.DataLoader;

public abstract class CommLoadingFragment<T> extends BaseLoadingFragment<T> {

    @Override
    public DataLoader initLoader() {
        return new CommDataLoader<T>(this, this) {
        };
    }
}
