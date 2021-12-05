package android.androidlib.mvp.base;

import android.androidlib.utils.LogUtil;

import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class BasePresenter<V extends BaseView,M extends  BaseModel> implements BaseLifecycleObserver{
    // 目前两个两个公用方法 ，传递的时候 会有不同的 View ，使用泛型
    private WeakReference<V> mViewReference;

    // 动态创建的 model 的对象
    private M mModel;

    private V mProxyView;
    // View 一般都是 Activity 或者 fragment ,涉及到内存泄漏，但是已经解绑了不会，如果没解绑就会泄漏

//
//    public BasePresenter getActivity() {
//        return this;
//    }

    // View 有一个特点，都是接口
    // GC 回收的算法机制（哪几种）标记清楚法
    public void attach(V view){
        this.mViewReference = new WeakReference<V>(view);

        // 用代理对象
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 执行这个方法 ，调用的是被代理的对象
                if(mViewReference == null||mViewReference.get() == null) {
                    return null;
                }
                // 没解绑执行的是原始被代理 View 的方法
                return method.invoke(mViewReference.get(),args);
            }
        });
        // 获取 Class 对象
        Type[] params = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        try {
            // 最好要判断一下类型
            mModel = (M) ((Class)params[1]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void detach(){
        // 不解绑的问题 Activity -> Presenter  ,Presenter 持有了 Activity 应该是会有内存泄漏
        this.mViewReference.clear();
        this.mViewReference = null;
        // 注释
        // this.mProxyView = null;
    }

    public M getModel() {
        return mModel;
    }

    public V getView() {
        return mProxyView;
    }

    @Override
    public void onAny(LifecycleOwner owner) {
        LogUtil.d("onAny");

    }

    @Override
    public void onCreate(LifecycleOwner owner) {

        LogUtil.d("LifecycleOwner onCreate");
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        LogUtil.d("LifecycleOwner onStart");

    }

    @Override
    public void onStop(LifecycleOwner owner) {
        LogUtil.d("LifecycleOwner onStop");

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onDestory(LifecycleOwner owner) {

    }
}
