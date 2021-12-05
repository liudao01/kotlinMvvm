package android.androidlib.base;

import android.androidlib.utils.Utils;
import android.app.Activity;

import androidx.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuml
 * @explain 重构
 * @time 2017/12/8 14:12
 */

public abstract class IBaseApplication extends MultiDexApplication {
    private static IBaseApplication baseApplication;
    private List<Activity> mActivityList = new ArrayList<Activity>();

    public IBaseApplication() {
        baseApplication = this;
    }

    public static IBaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

    public void addActivity(Activity activity) {
        try {
            // 新加进来的Activity进行主题的设置
            synchronized (this) {
                mActivityList.add(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            // Log.e(TAG, "remove--" + mActivityList.size());
            mActivityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        synchronized (this) {
            for (Activity activity : mActivityList) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        synchronized (this) {
            for (Activity a : mActivityList) {
                a.finish();
            }
            mActivityList.clear();
            // resetToken();
        }
    }


}
