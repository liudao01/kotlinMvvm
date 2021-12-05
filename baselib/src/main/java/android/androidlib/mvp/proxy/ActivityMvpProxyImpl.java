package android.androidlib.mvp.proxy;


import android.androidlib.mvp.base.BasePresenter;
import android.androidlib.mvp.base.BaseView;

import java.util.List;


public class ActivityMvpProxyImpl<V extends BaseView> extends MvpProxyImpl<V> implements ActivityMvpProxy{
    public ActivityMvpProxyImpl(V view) {
        super(view);
    }
    // 不同对待，一般可能不会



}
