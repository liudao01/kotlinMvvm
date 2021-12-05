package android.androidlib.net;

import java.util.Map;

/**
 * Created by liuml on 2021/4/26 11:25
 */
public class NetworkOption {

    /**
     * 网络请求的 TAG
     */
    public String mBaseUrl;
    public String mTag;
    public boolean isShowLoading;
    public boolean isShowToast;
    public Map<String,String> mHeaders;

//    public NetworkOption(String tag) {
//        this.mTag = tag;
//    }


    public static final  class Builder{
        public String tag;
        public Map<String,String> mHeaders;
        public String mBaseUrl;
        public boolean isShowLoading;
        public boolean isShowToast;

        public Builder setShowLoading(boolean showLoading) {
            isShowLoading = showLoading;
            return this;
        }

        public Builder setShowToast(boolean showToast) {
            isShowToast = showToast;
            return this;
        }

        public Builder setTag(String tag){
            this.tag=tag;
            return this;
        }

        public Builder setHeaders(Map<String,String> headers){
            mHeaders=headers;
            return this;

        }

        public Builder setBaseUrl(String baseUrl) {
            mBaseUrl = baseUrl;
            return this;
        }

        public NetworkOption build(){
            NetworkOption networkOption = new NetworkOption();
            networkOption.mHeaders=mHeaders;
            networkOption.mBaseUrl=mBaseUrl;
            networkOption.isShowLoading=isShowLoading;
            networkOption.isShowToast=isShowToast;
            return networkOption;
        }
    }
}

