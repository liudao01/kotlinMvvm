package android.androidlib.net;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author liuml
 * @explain
 * @time 2017/12/16 19:37
 */

public abstract class HttpEngine {


    public abstract void get(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog);

    public abstract void post(Context context, String url, String jsonParams, HttpCallBack callBack, boolean showDialog);

    public abstract void post(Context context, String url, String jsonParams, HttpCallBack callBack);

    public abstract void post(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog);

    public abstract void post(Context context, String url, Map<String, String> params, HttpCallBack callBack, boolean showDialog, boolean isShowToast);

    public abstract void post(Context context, String url, Map<String, String> params, HttpCallBack callBack);

    public abstract void post(Context context, String url, Map<String, String> params, NetworkOption option, HttpCallBack callBack);

    public abstract void uploading(Context context, String url, Map<String, String> file, Map<String, String> params, HttpCallBack callBack, boolean showDialog);

    public abstract void uploadingFiles(Context context, String url, String key, List<File> fileList, Map<String, String> params, HttpCallBack callBack, boolean showDialog);

    public abstract void download(Context context, String url, String dirPath, String fileName, HttpCallBack callBack, boolean showDialog);

    public abstract void removeAll();

    public abstract void removeTag(Object obj);





}
