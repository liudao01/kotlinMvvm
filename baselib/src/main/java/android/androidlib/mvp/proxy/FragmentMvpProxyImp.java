package android.androidlib.mvp.proxy;

import android.androidlib.mvp.base.BaseView;

/**
 * Created by liuml on 3/13/21 17:45
 */
public class FragmentMvpProxyImp<V extends BaseView> extends MvpProxyImpl<V> implements FragmentMvpProxy {
    public FragmentMvpProxyImp(V view) {
        super(view);
    }
}
