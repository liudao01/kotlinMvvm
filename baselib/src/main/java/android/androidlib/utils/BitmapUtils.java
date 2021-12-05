package android.androidlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liuml on 2/3/21 16:31
 */
public final class BitmapUtils {

    /**
     * 清空ImageView中的图片的内存
     */
    public static void clearImageMemory(View view) {
        if (view != null && view instanceof ImageView) {
            Drawable d = ((ImageView) view).getDrawable();
            if (d != null && d instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) d).getBitmap();
                if (bmp != null) {
                    bmp.recycle();
//                    System.gc();
                }
            }
            ((ImageView) view).setImageDrawable(null);
            if (d != null) {
                d.setCallback(null);
            }
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context Context
     * @param resId   图片资源ID
     * @return Bitmap
     */
    public static Bitmap getBitmapFromResource(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        /**
         * 1）inPurgeable：设置为True时，表示系统内存不足时可以被回 收，设置为False时，表示不能被回收。
         *
         * 2）inInputShareable：设置是否深拷贝，与inPurgeable结合使用，inPurgeable为false时，
         * 该参数无意义True：。
         */
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 转换Drawable到Bitmap
     *
     * @param drawable Drawable
     * @return 转换Drawable到Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        return null;
    }


    /**
     * 通过BitmapDrawable来获取Bitmap
     *
     * @param mContext
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromBitmapDrawable(Context mContext, String fileName) {
        BitmapDrawable bmpMeizi = null;
        try {
            bmpMeizi = new BitmapDrawable(mContext.getAssets().open(fileName));//"pic_meizi.jpg"
            Bitmap mBitmap = bmpMeizi.getBitmap();
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过资源ID获取Bitmap
     *
     * @param res
     * @param resId
     * @return
     */
    public static Bitmap webweb(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 通过文件路径来获取Bitmap
     *
     * @param pathName
     * @return
     */
    public static Bitmap getBitmapFromFile(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 通过字节数组来获取Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 通过输入流InputStream来获取Bitmap
     *
     * @param inputStream
     * @return
     */
    public static Bitmap getBitmapFromStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }



    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 设置缩略图
     *
     * @param bitMap
     * @param needRecycle
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 80;
        int newHeight = 80;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        if (needRecycle) bitMap.recycle();
        return newBitMap;
    }
}