package android.androidlib.mvp.base;

import android.androidlib.base.ICallback;
import android.androidlib.mvp.proxy.ActivityMvpProxy;
import android.androidlib.mvp.proxy.ActivityMvpProxyImpl;
import android.androidlib.ui.view.viewstatus.VaryViewHelperController;
import android.androidlib.utils.LogUtil;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import java.util.List;

public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements ICallback, BaseView {

    private VaryViewHelperController mVaryViewHelperController = null;


    public Context mContext;

    public ActivityMvpProxy activityMvpProxy;
    private List<BasePresenter> presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {


            if (getViewBinding() != null) {
                setContentView(getViewBinding());
            } else {
                setContentView(setLayoutId());
            }

            mContext = this;
            activityMvpProxy = createMyPreaters();

            activityMvpProxy.bindAndCreatePresenter();
            //必须放在bind后面
            presenter = activityMvpProxy.getPresenter();
            LogUtil.d("presenter = " + presenter);

            for (BasePresenter p : presenter) {
                Lifecycle lifecycle = getLifecycle();
                LogUtil.d("lifecycle = " + lifecycle);
                getLifecycle().addObserver(p);
            }
            initVariables();
            initViews(savedInstanceState);
            doBusiness();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
    }

    /**
     * 获取ViewBinding
     */
    protected abstract View getViewBinding();


    private ActivityMvpProxy createMyPreaters() {
        //mPresenters
        if (activityMvpProxy == null) {
            activityMvpProxy = new ActivityMvpProxyImpl<>(this);
        }
        return activityMvpProxy;

    }

//    protected abstract VB onCreateViewBinding( LayoutInflater layoutInflater);


    public abstract void initVariables();

    /**
     * 如果不使用ViewBinding，需要重写此方法传入布局Id,并重写isOpenViewBinding返回false
     */
    public abstract int setLayoutId();

    /**
     * 加载 layout 布局文件，初始化控件，为控件挂上事件方法。
     *
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 业务操作. 比如调用 MobileAPI 获取数据。
     */
    public abstract void doBusiness();


    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 一定要解绑
        activityMvpProxy.unbindPresenter();
    }


    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(int img, boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(img, msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    protected void toggleRestore() {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        mVaryViewHelperController.restore();
    }

    /**
     * 绑定空布局的view
     *
     * @param view
     */
    public void bindEmptyView(View view) {
        if (null != view) {
            mVaryViewHelperController = new VaryViewHelperController(view);
        }
    }
}
