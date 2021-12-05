package android.androidlib.mvp.base;

import android.androidlib.mvp.proxy.FragmentMvpProxy;
import android.androidlib.mvp.proxy.FragmentMvpProxyImp;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author liuml
 * @explain
 * @time 2017/12/8 10:46
 */

public abstract class BaseMvpFragment <P extends BasePresenter> extends Fragment implements  BaseView {

    FragmentMvpProxy fragmentMvpProxy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentMvpProxy = createMyPreaters();
        fragmentMvpProxy.bindAndCreatePresenter();
    }

    private FragmentMvpProxy createMyPreaters() {
        //mPresenters
        if (fragmentMvpProxy == null) {
            fragmentMvpProxy = new FragmentMvpProxyImp(this);
        }
        return fragmentMvpProxy;

    }



    /**
     * 获取ViewBinding
     */
    protected abstract View getViewBinding();
//
//
//    public abstract void initVariables();
//
//    /**
//     * 加载 layout 布局文件，初始化控件，为控件挂上事件方法。
//     *
//     * @param savedInstanceState
//     */
//    public abstract void initViews(Bundle savedInstanceState);
//
//
//    /**
//     * 业务操作. 比如调用 MobileAPI 获取数据。
//     */
//    public abstract void doBusiness();
//
//    public abstract int setLayoutId();

    @Override
    public void onDestroy() {
        super.onDestroy();
//        // 一定要解绑
        fragmentMvpProxy.unbindPresenter();
    }

}
