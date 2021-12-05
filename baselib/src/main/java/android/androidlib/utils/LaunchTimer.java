package android.androidlib.utils;

/**
 * Created by liuml on 1/29/21 16:13
 * 运行时间计算工具类
 * 开始时间
 * 结束时间
 *
 */
public class LaunchTimer {

    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        endRecord("");
    }

    /**
     * 重载一个方法 打印信息
     * @param msg
     */
    public static void endRecord(String msg) {
        //花费时间
        long cost = System.currentTimeMillis() - sTime;
        LogUtil.d(msg + " cost " + cost + "ms");
    }

    /**
     * 结束录制并返回花费时间
     * @return
     */
    public static long endRecordAndgetConsumeTime() {
        long cost = System.currentTimeMillis() - sTime;
        return cost;

    }

}