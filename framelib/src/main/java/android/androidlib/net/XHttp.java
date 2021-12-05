package android.androidlib.net;


import android.content.Context;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class XHttp extends HttpEngine {

    private static HttpEngine httpEngine;
    private static XHttp xHttp;
//    public static Handler handler = new Handler();

    public static void init(HttpEngine engine) {
        httpEngine = engine;
    }

    public static XHttp getInstance() {
        if (httpEngine == null) {
            throw new NullPointerException("Call XFrame.initXHttp(IHttpEngine httpEngine) within your Application onCreate() method." +
                    "Or extends XApplication");
        }
        if (xHttp == null) {
            xHttp = new XHttp();
        }
        return xHttp;
    }

    /**
     * 获取实体类的类型
     *
     * @param obj
     * @return
     */
    public static Class<?> analysisClassInfo(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog) {
        httpEngine.get(context, url, params, callBack, showDialog);
    }

    @Override
    public void post(Context context, String url, String jsonParams, HttpCallBack callBack, boolean showDialog) {
        httpEngine.post(context, url, jsonParams, callBack, showDialog);
    }

    @Override
    public void post(Context context, String url, String jsonParams, HttpCallBack callBack) {
        httpEngine.post(context, url, jsonParams, callBack, true);
    }

    /**
     * @param context
     * @param url
     * @param params
     * @param callBack
     * @param showDialog
     */
    @Override
    public void post(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog) {
        httpEngine.post(context, url, params, callBack, showDialog);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog, boolean isShowToast) {
        httpEngine.post(context, url, params, callBack, showDialog, isShowToast);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, HttpCallBack callBack) {
        post(context, url, params, callBack, true);
    }

    @Override
    public void uploading(Context context, String url, Map<String, String> files, Map<String, String> params, HttpCallBack callBack, boolean showDialog) {
        httpEngine.uploading(context, url, files, params, callBack, showDialog);
    }

    @Override
    public void uploadingFiles(Context context, String url, String key, List<File> fileList, Map<String, String> params, HttpCallBack callBack, boolean showDialog) {
        httpEngine.uploadingFiles(context, url, key, fileList, params, callBack, showDialog);
    }

    @Override
    public void download(Context context, String url, String dirPath, String fileName, HttpCallBack callBack, boolean showDialog) {
        httpEngine.download(context, url, dirPath, fileName, callBack, showDialog);
    }

    @Override
    public void removeAll() {
        httpEngine.removeAll();
    }

    @Override
    public void removeTag(Object obj) {
        httpEngine.removeTag(obj);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, NetworkOption option, HttpCallBack callBack) {
        httpEngine.post(context, url, params, option, callBack);
    }

//    public void post(String url, Map<String, String> params, HttpCallBack callBack) {
//        httpEngine.post(url,params,callBack);
//    }
}
