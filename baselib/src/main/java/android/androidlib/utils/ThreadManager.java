package android.androidlib.utils;

/**
 * @author liuml
 * @explain
 * @time 2017/12/4 15:40
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @explain 线程池工具类
 * @author liuml.
 * @time 2017/12/4 15:41
 */
public class ThreadManager {
    //参考AsyncTask 如何创建核心线程  Runtime.getRuntime().availableProcessors()返回的是可用的计算资源，而不是CPU物理核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    // 定义两个池子，mNormalPool 访问网络用的，mDownloadPool 是下载用的
    private static ThreadPoolProxy mNormalPool = new ThreadPoolProxy(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 5 * 1000);//param 0  最大线程数，param 1 核心线程数
    private static ThreadPoolProxy mDownloadPool = new ThreadPoolProxy(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 5 * 1000);


    // proxy 是代理的意思
    // 定义两个get方法，获得两个池子的对象 ，直接get 获得到的是代理对象
    public static ThreadPoolProxy getNormalPool() {
        return mNormalPool;
    }

    public static ThreadPoolProxy getDownloadPool() {
        return mDownloadPool;
    }

    // 代理设计模式类似一个中介，所以在中介这里有我们真正想获取的对象
    // 所以要先获取代理，再获取这个线程池
    public static class ThreadPoolProxy {
        private final int mCorePoolSize;     // 核心线程数
        private final int mMaximumPoolSize;  // 最大线程数
        private final long mKeepAliveTime;    // 所有任务执行完毕后普通线程回收的时间间隔
        private ThreadPoolExecutor mPool;  // 代理对象内部保存的是原来类的对象

        /**
         * @param corePoolSize 核心线程池大小
         * @param maximumPoolSize   最大线程池大小
         * @param keepAliveTime 保持存活的时间  线程空闲时间 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
         */
        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
        }

        private void initPool() {
            if (mPool == null || mPool.isShutdown()) {
                //                int corePoolSize = 1;//核心线程池大小
                //                int maximumPoolSize = 3;//最大线程池大小
                //                long keepAliveTime = 5 * 1000;//保持存活的时间
                TimeUnit unit = TimeUnit.MILLISECONDS;//单位
                BlockingQueue<Runnable> workQueue = null;//阻塞队列
                workQueue = new ArrayBlockingQueue<Runnable>(3);//FIFO,大小有限制，为3个
                //workQueue = new LinkedBlockingQueue();  //队列类型为linked，其大小不定，无限大小
                //                workQueue = new PriorityBlockingQueue();
                ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂
                RejectedExecutionHandler handler = null;//异常捕获器
                //                handler = new ThreadPoolExecutor.DiscardOldestPolicy();//去掉队列中首个任务，将新加入的放到队列中去
                //                handler = new ThreadPoolExecutor.AbortPolicy();//触发异常
                handler = new ThreadPoolExecutor.DiscardPolicy();//不做任何处理
                //                handler = new ThreadPoolExecutor.CallerRunsPolicy();//直接执行，不归线程池控制,在调用线程中执行
                //                new Thread(task).start();
                // 创建线程池
                mPool = new ThreadPoolExecutor(mCorePoolSize,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler);
            }
        }

        /**
         * 执行任务
         *
         * @param task
         */
        public void execute(Runnable task) {
            initPool();

            //执行任务
            mPool.execute(task);
        }

        // 提交任务
        public Future<?> submit(Runnable task) {
            initPool();
            return mPool.submit(task);
        }

        // 取消任务
        public void remove(Runnable task) {
            if (mPool != null && !mPool.isShutdown()) {
                mPool.getQueue()
                        .remove(task);
            }
        }
    }
}