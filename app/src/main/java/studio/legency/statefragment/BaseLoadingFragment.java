package studio.legency.statefragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import studio.legency.statefragment.adapter.BaseListAdapter;


public abstract class BaseLoadingFragment<T> extends ContentFragment implements
        DataLoadView<T>, RequestCallBack {

    protected static final int LISTVIEW = R.layout.loading_listview;

    protected static final int EXPANDLIST = R.layout.loading_expandlist;

    protected static final int EXPANDLIST_DIVIDER = R.layout.loading_expandlist_divider;

    protected static final int FLOATING_GROUP_EXPANDABLE_LISTVIEW = R.layout.floating_group_expandable_listview;

    protected boolean network_error;

    protected boolean login_needed;

    protected boolean auto_load_data = true;

    DataLoader loader;

    private Handler handler_ = new Handler(Looper.getMainLooper());

    private View network;

    private View need_login;

    private AdapterView adapterView;

    private boolean need_adapter = true;

    /**
     * @return
     */
    public abstract T dataToLoad();

    /**
     * @param result
     */
    public abstract void dataLoaded(T result);

    /**
     * @return
     */
    public abstract DataLoader initLoader();

    /**
     * @return
     */
    public abstract int getContentView();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentShown(false);
        loader = initLoader();
        if (auto_load_data)
            executeLoad();
    }

    public void executeLoad() {
        loader.executeLoad();
    }

    @Override
    public T startLoadData() {
        network_error = false;
        login_needed = false;
        T result = null;
        try {
            result = dataToLoad();
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public void loaderFinished(T result) {
        if (network_error) {
            network.setVisibility(View.VISIBLE);
            setEmptyView();
        } else {
            network.setVisibility(View.GONE);
            if (result == null
                    || ((result instanceof List) && ((List) result).size() == 0 || resultEqualEmpty(result))) {
                setEmptyView();
            } else {
                setNotEmptyView();
                dataLoaded(result);
            }
        }
        if (need_login != null)
            if (login_needed) {
                need_login.setVisibility(View.VISIBLE);
            } else {
                need_login.setVisibility(View.GONE);
            }
        if (isResumed()) {
            setContentShown(true);
        } else {
            setContentShownNoAnimation(true);
        }
    }

    /**
     * 子类中复写方法 判断reslut 不为空时需要显示为空的条件
     *
     * @param result
     * @return
     */
    protected boolean resultEqualEmpty(T result) {
        return false;
    }

    /**
     * outer layout id default is loading_fragment
     *
     * @return
     */
    protected int getCustomLayout() {
        return R.layout.loading_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View loadingView = inflater
                .inflate(getCustomLayout(), container, false);
        FrameLayout viewContainer = (FrameLayout) loadingView
                .findViewById(R.id.view_container);
        addOnViewContainer(viewContainer, inflater);
        ViewStub customView = (ViewStub) loadingView
                .findViewById(R.id.custom_view);
        setCustomView(customView);
        customView.inflate();
        adapterView = (AdapterView) loadingView.findViewById(INTERNAL_CONTENT_ID);
        if (adapterView != null) {
            setListHeaderAndFooter(inflater);
        }
        return loadingView;
    }

    /**
     * add more view
     *
     * @param viewContainer
     * @param inflater
     */
    protected void addOnViewContainer(FrameLayout viewContainer,
                                      LayoutInflater inflater) {

    }

    protected boolean isScrollEnabled() {
        return false;
    }

    public void setListHeaderAndFooter(LayoutInflater inflater) {
        inflateListViewExtend(inflater, null, null);
    }

    public void addHeaderView(View v) {
        if (adapterView instanceof ListView)
            ((ListView) adapterView).addHeaderView(v);
    }

    public void addFooterView(View v) {
        if (adapterView instanceof ListView)
            ((ListView) adapterView).addHeaderView(v);
    }

    public AdapterView getListView() {
        return adapterView;
    }

    public void setAdapter(Adapter adapter) {
        if (adapterView != null)
            adapterView.setAdapter(adapter);
    }

    public void setExAdapter(ExpandableListAdapter adapter) {
        if (adapterView instanceof ExpandableListView) {
            ((ExpandableListView) adapterView).setAdapter(adapter);
        } else {
            throw new RuntimeException(
                    "the view you set is not ExpandableListView");
        }
    }

    /**
     * override this  method and add the layout you want to add call super.
     *
     * @param inflater
     * @param listHeaderViewId listView custom header view id
     * @param listFooterViewId listView custom footer view id
     */
    protected void inflateListViewExtend(LayoutInflater inflater,
                                         Integer listHeaderViewId, Integer listFooterViewId
    ) {
        if (adapterView == null)
            return;
        if (listHeaderViewId != null) {
            View headerView = inflater.inflate(listHeaderViewId, null, true);
            addHeaderView(headerView);
        }
        if (listFooterViewId != null) {
            View footerView = inflater.inflate(listFooterViewId, null, true);
            addHeaderView(footerView);
        }
    }

    private void setCustomView(ViewStub customView) {
        customView.setLayoutResource(getContentView());
    }

    /**
     * set the view to @LoadingFragment
     *
     * @param root
     */
    void setView(View root) {
        network = (root.findViewById(R.id.network_toast));
        network.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loader.reload();
                setContentShown(false);
                network.setVisibility(View.GONE);
            }
        });

        need_login = (root.findViewById(R.id.need_login));
    }

    @Override
    public void loadFailed(Exception arg0) {
        loaderFinished(null);
    }

    @Override
    public void networkError() {
        LogUtils.d("net work error");
        network_error = true;
    }

    @Override
    public void loginNeeded() {
        login_needed = true;
    }

    @Override
    public void failedWithError(String error_message) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setView(view);
        if (need_adapter)
            setAdapter(initAdapter());
        super.onViewCreated(view, savedInstanceState);
    }

    public Adapter initAdapter() {
        return new BaseListAdapter<T>() {

            @Override
            protected View getView(T o, View convertView, ViewGroup parent) {
                return getView(o, convertView, parent);
            }
        };
    }

    /**
     * 当need_adapter 设置true时 需要复写
     *
     * @param o
     * @param convertView
     * @param parent
     * @return
     */
    protected View getView(T o, View convertView, ViewGroup parent) {
        return null;
    }

    public void reload() {
        reload(true);
    }

    /**
     * reload data
     *
     * @param showLoadingBar showLoadingBar or not
     */
    public void reload(boolean showLoadingBar) {
        if (showLoadingBar)
            handler_.post(new Runnable() {

                @Override
                public void run() {
                    setContentShown(false);
                }

            });

        loader.reload();
    }
}