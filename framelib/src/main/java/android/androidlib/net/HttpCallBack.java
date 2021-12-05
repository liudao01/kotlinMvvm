package android.androidlib.net;

/**
 * @author liuml
 * @explain http回调
 * @time 2017/12/16 19:31
 */


public abstract class HttpCallBack<Result> implements BaseHttpCallBack<Result> {

    @Override
    public void onProgress(long bytesDownloaded, long totalBytes) {

    }

    @Override
    public void progress(int progress) {

    }

    @Override
    public void requestAgain() {

    }

    @Override
    public void onFailed(int errorCode, String error) {

    }

    @Override
    public void onFailed(String errorCode, String error) {

    }

}