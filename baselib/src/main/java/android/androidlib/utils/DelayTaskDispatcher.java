package android.androidlib.utils;

/**
 * Created by liuml on 3/2/21 15:28
 */
import android.androidlib.utils.starter.tool.task.Task;
import android.os.Looper;
import android.os.MessageQueue;

import java.util.LinkedList;
import java.util.Queue;

public class DelayTaskDispatcher {
    private Queue<Task> delayTasks = new LinkedList<>();

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            if (delayTasks.size() > 0) {
                Task task = delayTasks.poll();
                if (task != null) {
                    task.run();
                }
            }
            return !delayTasks.isEmpty(); //delayTasks非空时返回ture表示下次继续执行，为空时返回false系统会移除该IdleHandler不再执行
        }
    };

    public DelayTaskDispatcher addTask(Task task) {
        delayTasks.add(task);
        return this;
    }

    public void start() {
        Looper.myQueue().addIdleHandler(idleHandler);
    }
}