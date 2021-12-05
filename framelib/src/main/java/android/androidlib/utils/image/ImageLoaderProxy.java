package android.androidlib.utils.image;

import android.androidlib.base.IBaseApplication;
import android.content.Context;
import android.widget.ImageView;

/**
 * @author liuml
 * @explain 代理
 * @time 2017/12/16 10:55
 */

public class ImageLoaderProxy implements Imageloader {

    /**
     * 使用
     */
    //ImageLoaderProxy.getInstance().displayImage(imageUrl, imageView, R.mipmap.ic_default);
    private Imageloader imageLoader;

    private static volatile ImageLoaderProxy instance;

    public static ImageLoaderProxy getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderProxy.class) {
                if (instance == null) {
                    instance = new ImageLoaderProxy();
                }
            }
        }
        return instance;
    }

    public ImageLoaderProxy() {
//        imageLoader = new GlideImageLoader();//可任意替换
        init(IBaseApplication.getInstance());
    }

    @Override
    public void init(Context context) {
//        imageLoader.init(context);
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
//        imageLoader.displayImage(imageUrl, imageView, defaultImage);
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView) {
//        imageLoader.displayImage(imageUrl, imageView);
    }
}
