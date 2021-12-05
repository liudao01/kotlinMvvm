package android.androidlib.utils.starter;

import android.androidlib.utils.starter.tool.task.DispatchRunnable;
import android.androidlib.utils.starter.tool.task.Task;
import android.os.Looper;
import android.os.MessageQueue;


import java.util.LinkedList;
import java.util.Queue;

/**
 * 延迟加载
 * 通过IdleHandler 利用程序空闲时间，进行加载操作，此方式能极大提高响应效率
 */
public class DelayInitDispatcher {

    private Queue<Task> mDelayTasks = new LinkedList<>();

    private MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            if(mDelayTasks.size()>0){
                Task task = mDelayTasks.poll();
                new DispatchRunnable(task).run();
            }
            return !mDelayTasks.isEmpty();
        }
    };

    public DelayInitDispatcher addTask(Task task){
        mDelayTasks.add(task);
        return this;
    }

    public void start(){
        Looper.myQueue().addIdleHandler(mIdleHandler);
    }

}
