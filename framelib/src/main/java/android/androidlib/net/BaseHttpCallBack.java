package android.androidlib.net;

/**
 * @author liuml
 * @explain http回调
 * @time 2017/12/16 19:31
 */


public interface BaseHttpCallBack<Result> {

    void onProgress(long bytesDownloaded, long totalBytes);
    void progress(int progress);

    void onSuccess(Result result);

    void onFailed(int errorCode, String error);

    void onFailed(String errorCode, String error);

    void requestAgain();
}