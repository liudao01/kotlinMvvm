package android.androidlib.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.SystemClock;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by liuml on 3/18/21 16:43
 */
public class MemoryUtil {
    private ActivityManager activityManager = null;
    // ProcessInfo Model类 用来保存所有进程信息

    private MemoryUtil() {
    }

    private static class MemoryUtilHoler {
        private static MemoryUtil memoryUtil = new MemoryUtil();
    }

    public static MemoryUtil getinstance() {
        return MemoryUtilHoler.memoryUtil;
    }


    public void startMonitorMemory(final Application application) {
        ThreadManager.getDownloadPool().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    printMemoryInfo(application);
                    SystemClock.sleep(1000);
                }
            }
        });
    }

    private void printMemoryInfo(Application application) {
        String tag = "APPActivity";
        activityManager = (ActivityManager) application.getSystemService(ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(info);

//        LogUtil.d("系统剩余内存:" + (info.availMem >> 10) + "k");
//        LogUtil.d("系统是否处于低内存运行：" + info.lowMemory);
//        LogUtil.d("当系统剩余内存低于" + (info.threshold >> 10) + "时就看成低内存运行");

//        LogUtil.d("NativeHeapSizeTotal:" + (Debug.getNativeHeapSize() >> 10));
//        LogUtil.d("NativeAllocatedHeapSize:" + (Debug.getNativeHeapAllocatedSize() >> 10));
//        LogUtil.d("NativeAllocatedFree:" + (Debug.getNativeHeapFreeSize() >> 10));

        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

        double M = 1024.0 * 1024.0;
        Runtime r = Runtime.getRuntime();

        LogUtil.d("最大可用内存:----" + MathExtendUtils.divide(r.maxMemory(), M) + "M");
        LogUtil.d("当前可用内存:----" + MathExtendUtils.divide(r.totalMemory(), M) + "M");
        LogUtil.d("当前空闲内存:----" + MathExtendUtils.divide(r.freeMemory(), M) + "M");
        LogUtil.d("当前已使用内存:----" + MathExtendUtils.divide((r.totalMemory() - r.freeMemory()), M) + "M");


        ActivityManager manager = (ActivityManager) application.getSystemService(ACTIVITY_SERVICE);
        int i = manager.getMemoryClass();
        LogUtil.d("当前应用已使用内存:----" + i);

//        MathExtendUtils.multiply()
        LogUtil.d("最大可用内存:" + MathExtendUtils.divide(r.maxMemory(), M) + "M \n\n");
        LogUtil.d("------------------------------------------------------------------");
        getRunningAppProcessInfo();
        LogUtil.d("------------------------------------------------------------------");
    }

    public void doSomethingMemoryIntensive(Application application) {

        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory(application);

        if (!memoryInfo.lowMemory) {

        }
    }


//    private float getMemoryData() {
//        float mem = 0.0F;
//        try {
//            Debug.MemoryInfo memInfo = null;
//            //28 为Android P
//            if (Build.VERSION.SDK_INT > 28) {
//                // 统计进程的内存信息 totalPss
//                memInfo = new Debug.MemoryInfo();
//                Debug.getMemoryInfo(memInfo);
//            } else {
//                //As of Android Q, for regular apps this method will only return information about the memory info for the processes running as the caller's uid;
//                // no other process memory info is available and will be zero. Also of Android Q the sample rate allowed by this API is significantly limited, if called faster the limit you will receive the same data as the previous call.
//
//                Debug.MemoryInfo[] memInfos = mActivityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
//                if (memInfos != null && memInfos.length > 0) {
//                    memInfo = memInfos[0];
//                }
//            }
//
//            int totalPss = memInfo.getTotalPss();
//            if (totalPss >= 0) {
//                // Mem in MB
//                mem = totalPss / 1024.0F;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mem;
//    }

    private ActivityManager.MemoryInfo getAvailableMemory(Application application) {
        ActivityManager activityManager = (ActivityManager) application.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    // 获得系统进程信息
    private void getRunningAppProcessInfo() {
        // ProcessInfo Model类   用来保存所有进程信息

        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            // 进程ID号
            int pid = appProcessInfo.pid;
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            int uid = appProcessInfo.uid;
            // 进程名，默认是包名或者由属性android：process=""指定
            String processName = appProcessInfo.processName;
            // 获得该进程占用的内存
            int[] myMempid = new int[]{pid};
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            Debug.MemoryInfo[] memoryInfo = activityManager
                    .getProcessMemoryInfo(myMempid);
            // 获取进程占内存用信息 kb单位
            int memSize = memoryInfo[0].dalvikPrivateDirty;
            double divide = MathExtendUtils.divide(memSize, 1024.0);
            LogUtil.d("processName: " + processName + "  pid: " + pid
                    + " uid:" + uid + " memorySize is -->" + divide + "m");
            LogUtil.d("processName: " + processName + "  pid: " + pid
                    + " uid:" + uid + " memorySize is -->" + memSize + "kb");
            // 构造一个ProcessInfo对象


        }
    }

    /**
     * 获取当前的进程名
     *
     * @param context:上下文
     * @return :返回值
     */
    public ActivityManager.RunningAppProcessInfo getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo;
//                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 转化为int
     *
     * @param value        传入对象
     * @param defaultValue 发生异常时，返回默认值
     * @return
     */
    public final int convertToInt(Object value, int defaultValue) {

        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {

            try {
                return Integer.valueOf(String.valueOf(value));
            } catch (Exception e1) {

                try {
                    return Double.valueOf(value.toString()).intValue();
                } catch (Exception e2) {
                    return defaultValue;
                }
            }
        }
    }


}