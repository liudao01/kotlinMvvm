package android.androidlib.base;

import android.androidlib.ui.view.viewstatus.VaryViewHelperController;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author liuml
 * @explain 与业务无关的 Activity 基类
 * @time 2017/12/5 09:52
 */

public abstract class IBaseActivity extends AppCompatActivity implements ICallback {
    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //这里setContentView 必须放在最前面
        setContentView(setLayoutId());
        super.onCreate(savedInstanceState);
//        IBaseApplication.getInstance().addActivity(this);
        mContext = this;
        initVariables();
        initViews(savedInstanceState);
        doBusiness();
    }
//
//    /**
//     * 初始化Layout
//     */
    public abstract int setLayoutId();
//
//    /**
//     * 初始化变量，包括 Intent 带的数据和 Activity 内的变量。
//     */
    public abstract void initVariables();
//
//    /**
//     * 加载 layout 布局文件，初始化控件，为控件挂上事件方法。
//     *
//     * @param savedInstanceState
//     */
    public abstract void initViews(Bundle savedInstanceState);
//
//    /**
//     * 业务操作. 比如调用 MobileAPI 获取数据。
//     */
    public abstract void doBusiness();

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
    protected void toggleShowEmpty(int img,boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(img,msg, onClickListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        IBaseApplication.getInstance().finishActivity(this);
    }
}
