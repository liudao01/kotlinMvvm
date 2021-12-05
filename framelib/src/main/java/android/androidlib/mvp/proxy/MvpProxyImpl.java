package android.androidlib.mvp.proxy;


import android.androidlib.mvp.base.BasePresenter;
import android.androidlib.mvp.base.BaseView;
import android.androidlib.mvp.inject.InjectPresenter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * mvp具体实现
 *
 * @param <V>
 */
public class MvpProxyImpl<V extends BaseView> implements IMvpProxy {
    private V mView;
    private List<BasePresenter> mPresenters = null;

    public MvpProxyImpl(V view) {
        // 做一下判断 是不是 NULL
        this.mView = view;
        if (mPresenters == null) {
            mPresenters = new ArrayList<>();
        }
    }


    @Override
    public void bindAndCreatePresenter() {
        // 这个地方要去注入 Presenter 通过反射 (Dagger)也可以实现
        Field[] fields = mView.getClass().getDeclaredFields();//获得某个类的所有声明的字段
        for (Field field : fields) {
            //getAnnotation()方法用法实例教程。返回该元素的指定类型的注释，如果是这样的注释，否则返回null。
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null && field.isAnnotationPresent(InjectPresenter.class)) {
                // 创建注入
                Class<? extends BasePresenter> presenterClazz = null;
                // 自己去判断一下类型？ 获取继承的父类，如果不是 继承 BasePresenter 抛异常
                presenterClazz = (Class<? extends BasePresenter>) field.getType();//getType()方法返回一个Class对象
                if (!BasePresenter.class.isAssignableFrom(presenterClazz)) {
                    //isAssignableFrom()方法是判断是否为某个类的父类
                    // 这个 Class 是不是继承自 BasePresenter 如果不是抛异常
                    throw new RuntimeException("No support inject presenter type " + presenterClazz.getName());
                }
                BasePresenter basePresenter = null;

                try {
                    // 创建 Presenter 对象
                    basePresenter = presenterClazz.newInstance();

                    basePresenter.attach(mView);
                    // 设置权限 因为是私有的必须暴力反射
                    field.setAccessible(true);
                    //向对象的这个Field属性设置新值value
                    field.set(mView, basePresenter);
                    mPresenters.add(basePresenter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 检测 View 层是否实现了 BasePresenter 的View接口
                checkView(basePresenter);
            }
        }
    }

    /**
     * 检测 View 层是否实现了 BasePresenter 的View接口
     *
     * @param basePresenter
     */
    private void checkView(BasePresenter basePresenter) {
        // 1. Presenter 的 View 接口  getGenericSuperclass返回直接继承的父类  getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组
        Type[] params = ((ParameterizedType) basePresenter.getClass().getGenericSuperclass()).getActualTypeArguments();
        Class viewClazz = ((Class) params[0]);

        // 2. 要拿到 View 层的所有接口
        Class[] ViewClasses = mView.getClass().getInterfaces();
        // 3. View层没有实现就抛异常
        boolean isImplementsView = false;
        for (Class viewClass : ViewClasses) {
            if (viewClass.isAssignableFrom(viewClazz)) {
                isImplementsView = true;
            }
        }
        if (!isImplementsView) {
            throw new RuntimeException(mView.getClass().getSimpleName() + " must implements " + viewClazz.getName());
        }
    }

    @Override
    public void unbindPresenter() {
        // 一定要解绑
        for (BasePresenter presenter : mPresenters) {
            presenter.detach();
        }
        mView = null;
    }

    @Override
    public List getPresenter() {
        return mPresenters;
    }
}
