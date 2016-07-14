package demo.personal.com.cn.scheduler.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import demo.personal.com.cn.scheduler.util.Logger;


/**
 * 任务调度器，如果请求过多，将会排队等候，直到执行完成，每个任务都有线程池里的一个线程执行
 * 
 * @author RQHua
 *
 */
public class TaskScheduler extends Thread {
	private static TaskScheduler mScheduler = new TaskScheduler();

	/**
	 * 队列，链表结构，先进先出
	 */
	private LinkedBlockingQueue<Task> mTaskQueue = new LinkedBlockingQueue<Task>(20);
	/**
	 * 线程池
	 */
	private ExecutorService service = Executors.newFixedThreadPool(30);

	public static TaskScheduler getInstance() {
		return mScheduler;
	}

	public void run() {
		Task task = null;
		while (true) {
			try {
				task = mTaskQueue.take();
				service.submit(task);
			} catch (Exception e) {
				Logger.error("调度器发生异常", e);
			}
		}
	}

	/**
	 * 向任务队列添加一个任务元素
	 * 
	 * @param task
	 */
	public void addTask(Task task) {
		if (task == null) {
			return;
		}
		try {
			mTaskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
