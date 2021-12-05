package android.androidlib.utils.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author liuml
 * @explain
 * @time 2017/12/16 10:53
 */

public interface Imageloader {
    //初始化
    void init(Context context);

    /**
     * 显示图片
     *
     * @param imageUrl     图片地址
     * @param imageView    显示图片的ImageView
     * @param defaultImage 默认图片
     */
    void displayImage(String imageUrl, ImageView imageView, int defaultImage);
    /**
     * 显示图片
     *
     * @param imageUrl     图片地址
     * @param imageView    显示图片的ImageView
     */
    void displayImage(String imageUrl, ImageView imageView);
}
