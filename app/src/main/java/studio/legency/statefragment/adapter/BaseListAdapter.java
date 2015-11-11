package studio.legency.statefragment.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by legency on 2015/10/18.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    List<T> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T t = getItem(position);
        return getView(t, convertView, parent);
    }

    protected abstract View getView(T t, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        this.list = list;
    }
}
