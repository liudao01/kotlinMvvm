package android.androidlib.utils;

import android.androidlib.R;
import android.androidlib.utils.gif.GlideApp;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.rastermill.FrameSequenceDrawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

/**
 * 图片加载工具类
 * Created by Administrator on 2017/12/5 0005.
 */

public class GlideUtils {

    private static final String TAG = "ImageLoaderUtils";
    
    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadRoundedCorner(Context context, ImageView imageView, String url) {
        if (Utils.isDestroy((Activity) context)) return;
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
//                .bitmapTransform(roundedCorners)
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
//                .bitmapTransform(new RoundedCorners(10))
                .transform(new CenterCrop(), new GlideRoundTransform(context, 4))
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(R.drawable.bg_gray_empty_rounder)
//                .skipMemoryCache(true)                      //禁止Glide内存缓存
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadRoundedCornerLow(Context context, ImageView imageView, String url) {
        if (Utils.isDestroy((Activity) context)) return;
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
//                .bitmapTransform(roundedCorners)
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
//                .bitmapTransform(new RoundedCorners(10))
                .transform(new CenterCrop(), new GlideRoundTransform(context, 4))
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(R.drawable.bg_gray_empty_rounder)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param
     */
    public static void loadRoundedCorner(Context context, ImageView imageView, int resId) {
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
//                .bitmapTransform(roundedCorners)
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
//                .bitmapTransform(new RoundedCorners(10))
                .transform(new CenterCrop(), new GlideRoundTransform(context, 4))
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(R.drawable.bg_gray_empty_rounder)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        // 图片加载库采用Glide框架
        Glide.with(context).load(resId).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void loadFif(Context context, int resId, ImageView imageView) {
        if (Utils.isDestroy((Activity) context)) return;
        Glide.with(context).load(resId).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).into(imageView);
    }

    public static void loadFifLoop(Context context, int resId, ImageView imageView) {
        if (Utils.isDestroy((Activity) context)) return;

        GlideApp.with(context).as(FrameSequenceDrawable.class).load(resId).into(imageView);
//        Glide.with(context).load(resId).listener(new RequestListener() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
////                if (resource instanceof GifDrawable) {
//                //加载一次
////                    ((GifDrawable) resource).setLoopCount(1);
////                }
//                return false;
//            }
//        }).into(imageView);
    }

    /**
     * 加载Top圆角
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public static void loadTopRoundedCorner(Context context, ImageView imageView, String url, int radius) {

        if (Utils.isDestroy((Activity) context)) return;
        GlideCustomeRoundTransform transform = new GlideCustomeRoundTransform(context, radius);
        transform.setNeedCorner(true, true, false, false);
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
//                .bitmapTransform(roundedCorners)
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
//                .bitmapTransform(new RoundedCorners(10))
                .transform(new CenterCrop(), transform)
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(R.drawable.bg_gray_empty_rounder)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);


    }

    /**
     * 圆角图片加载
     *
     * @param context      上下文
     * @param imageView    图片显示控件
     * @param url          图片链接
     * @param defaultImage 默认占位图片
     * @param errorImage   加载失败后图片
     * @param radius       图片圆角半径
     * @return
     * @author leibing
     * @createTime 2016/8/15
     * @lastModify 2016/8/15
     */
    public static void load(Context context, ImageView imageView, String url, int defaultImage,
                            int errorImage, int radius) {
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .placeholder(defaultImage)
                .error(errorImage)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .transform(new CornersTranform(context, radius));
        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);

    }

    /**
     * 加载缩略图
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void smallDefaultLoad(Context context, ImageView imageView, String url) {
        load(context, imageView, url + "?x-oss-process=image/resize,m_fill,h_" + 200 + ",w_" + 200);
    }

    /**
     * 加载指定大小的缩略图
     *
     * @param context
     * @param imageView
     * @param url
     * @param height
     * @param width
     */
    public static void smallLoad(Context context, ImageView imageView, String url, int height, int width) {
        loadRoundedCorner(context, imageView, url + "?x-oss-process=image/resize,m_fill,h_" + height + ",w_" + width);
    }


    public static void load(Context context, ImageView imageView, String url) {

        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(R.drawable.bg_gray_empty_rounder)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);

    }

    public static void loadFullPhoto(Context context, ImageView imageView, String url, int errorId) {
        if (Utils.isDestroy((Activity) context)) return;
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .placeholder(R.drawable.bg_gray_empty_rounder)
                .error(errorId)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        // 图片加载库采用Glide框架
        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 加载resoures下的文件
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImgId(Context context, final ImageView imageView, int url, int defaultImage,
                                 int errorImage, int radius) {
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .placeholder(defaultImage)
                .error(errorImage)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .transform(new CornersTranform(context, radius));
        // 图片加载库采用Glide框架
        Glide.with(context).load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     * 加载圆形头像
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultImage
     * @param errorImage
     */
    public static void loadCircle(Context context, final ImageView imageView, String url, int defaultImage,
                                  int errorImage) {
        if (Utils.isDestroy((Activity) context)) return;

         Glide.with(context).load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }


    /**
     * 加载圆形头像
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultImage
     * @param errorImage
     */
    public static void loadCircleHeader(Context context, final ImageView imageView, String url, int defaultImage,
                                        int errorImage) {
        if (Utils.isDestroy((Activity) context)) return;


////
//        RequestOptions options = new RequestOptions()
//                // 但不保证所有图片都按序加载
//                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
//                // 默认为Priority.NORMAL
//                // 如果没设置fallback，model为空时将显示error的Drawable，
//                // 如果error的Drawable也没设置，就显示placeholder的Drawable
//                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
//                .signature(new ObjectKey(url))
//                .dontAnimate() //防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
//                .placeholder(defaultImage)
//                .error(errorImage)
//                // 缓存原始数据
////                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .centerCrop()
//                .transform(new GlideCircleTransform(context));
//        // 图片加载库采用Glide框架
//        Glide.with(context).load(url)
//                .apply(options)
//                .transition(new DrawableTransitionOptions().crossFade())
//                .thumbnail(0.1f)
//                .into(imageView);

        Glide.with(context).load(url).error(defaultImage)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }


    public static void loadCircleHeader(Context context, final ImageView imageView, int url, int defaultImage,
                                        int errorImage) {
        if (Utils.isDestroy((Activity) context)) return;
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .dontAnimate() //防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .placeholder(defaultImage)
                .error(errorImage)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .signature(new ObjectKey(url))
                .transform(new GlideCircleTransform(context));
        // 图片加载库采用Glide框架
        Glide.with(context).load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .thumbnail(0.1f)
                .into(imageView);
    }

    /**
     * 加载圆形头像
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultImage
     * @param errorImage
     */
    public static void loadCircle(Context context, final ImageView imageView, int url, int defaultImage,
                                  int errorImage) {
        if (Utils.isDestroy((Activity) context)) return;
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL) //指定加载的优先级，优先级越高越优先加载，
                .dontAnimate() //防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .placeholder(defaultImage)
                .error(errorImage)
                // 缓存原始数据
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .signature(new ObjectKey(url))
                .transform(new GlideCircleTransform(context));
        // 图片加载库采用Glide框架
        Glide.with(context).load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /*
     *加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     */
//    public static void loadCustRoundCircleImage(Context context, String url, ImageView imageView, RoundedCornersTransformation.CornerType type) {
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.color.white)
//                .error(R.color.white)
////.priority(Priority.HIGH)
//                .bitmapTransform(new RoundedCornersTransformation(45, 0, type))
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//
//        Glide.with(context).load(url).apply(options).into(imageView);
//    }

    public static void imageLoadPause(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void imageLoadresume(Context context) {
        Glide.with(context).resumeRequests();
    }

}