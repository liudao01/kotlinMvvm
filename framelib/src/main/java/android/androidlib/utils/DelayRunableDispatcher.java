package android.androidlib.utils;

import android.os.Looper;
import android.os.MessageQueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by liuml on 3/2/21 15:28
 */

public class DelayRunableDispatcher {
    //LinkedList 实现了Deque 接口 Deque接口继承了Queue 所以可以这样创建对象
    private Queue<Runnable> delayTasks = new LinkedList<>();

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            LogUtil.d("queueIdle调用");
            if (delayTasks.size() > 0) {
                LogUtil.d("从队列取出");
                Runnable runnable = delayTasks.poll();
                if (runnable != null) {
                    runnable.run();
                }
            }
            return !delayTasks.isEmpty();
            //delayTasks非空时返回ture表示下次继续执行
            //为空时返回false系统会移除该IdleHandler不再执行
        }
    };

    public DelayRunableDispatcher addTask(Runnable runnable) {
        delayTasks.add(runnable);
        return this;
    }

    public void start() {
        Looper.myQueue().addIdleHandler(idleHandler);
    }
}