package android.androidlib.mvp.proxy;

import android.androidlib.mvp.base.BasePresenter;

import java.util.List;

/**
 * Created by hcDarren on 2018/1/6.
 */

public interface IMvpProxy {
    void bindAndCreatePresenter();// 一个是和创建绑定
    void unbindPresenter();// 一个是解绑

    List<BasePresenter> getPresenter();
}
