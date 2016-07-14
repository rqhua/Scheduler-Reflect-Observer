package demo.personal.com.cn.scheduler.scheduler;

import java.util.HashMap;
import java.util.Map;

import demo.personal.com.cn.scheduler.action.base.AbstractAction;
import demo.personal.com.cn.scheduler.action.base.ActionCallback;
import demo.personal.com.cn.scheduler.action.base.ActionContainer;
import demo.personal.com.cn.scheduler.util.Logger;
import demo.personal.com.cn.scheduler.util.MVCException;


/**
 * 任务管理器，初始化后，UI通过此入口，向任务调度器提交任务
 */
public final class TaskManager {
	private static TaskManager mTaskManager = new TaskManager();

	/**
	 * 任务调度器
	 */
	private TaskScheduler mTaskScheduler = TaskScheduler.getInstance();
	/**
	 * 已经注册的所有的任务
	 */
	protected Map<String, Object> mActionContainer = new HashMap<String, Object>();
	/**
	 * 任务调度器是否已经开始调度工作
	 */
	private boolean isStart = false;

	private static TaskManager getInstance() {
		if (mTaskManager.isStart == false) {
			mTaskManager.start();
			mTaskManager.isStart = true;
		}
		return mTaskManager;
	}

	/**
	 * 开启调度器
	 */
	private void start() {
		this.mTaskScheduler.start();
	}

	/**
	 * 第一步：初始化ActionContainer
	 */
	public static void initActionContainer(ActionContainer actions) {
		actions.initActions();
		getInstance().mActionContainer.putAll(actions.getActions());
	}

	/**
	 * 提交任务，异步返回消息
	 * 
	 * @param actionUrl
	 *            访问action的url. 形式：actionId/methodName.（类名/方法名）
	 * @param param
	 *            参数
	 * @param callback
	 *            响应回调
	 */
	public static void doSubmit(String actionUrl, Map<String, Object> param, ActionCallback callback) {
		getInstance().newTask(actionUrl, param, callback);
	}

	/**
	 * 提交任务，异步返回消息
	 * 
	 * @param clazz
	 *            访问的类
	 * @param methodName
	 *            方法名称
	 * @param param
	 *            参数
	 * @param callback
	 *            响应回调
	 */
	public static void doSubmit(Class<? extends AbstractAction> clazz, String methodName, Map<String, Object> param,
								ActionCallback callback) {
		doSubmit(clazz.getName() + "/" + methodName, param, callback);
	}

	/**
	 * 提交任务，异步返回消息
	 * 
	 * @param clazz
	 *            访问的类
	 * @param methodName
	 *            方法名称
	 * @param param
	 *            参数
	 * @param callback
	 *            响应回调
	 * @return
	 */
	public static <T> T doSubmitForResult(Class<? extends AbstractAction> clazz, String methodName,
			Map<String, Object> param, ActionCallback callback) {
		return doSubmitForResult(clazz.getName() + "/" + methodName, param, callback);
	}

	/**
	 * 提交任务，异步返回消息
	 * 
	 * @param actionUrl
	 *            访问action的url. 形式：actionId/methodName.
	 * @param param
	 *            参数
	 * @param callback
	 *            响应回调
	 * @return 同步返回值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T doSubmitForResult(String actionUrl, Map<String, Object> param, ActionCallback callback) {
		Task task = getInstance().newTask(actionUrl, param, callback);
		return (T) task.getResult();
	}

	/**
	 * 统一初始化Task
	 */
	private Task newTask(String actionUrl, Map<String, Object> param, ActionCallback callback) {
		Task task = new Task();
		task.setActionUrl(actionUrl);
		task.setParam(param);
		task.setCallback(callback);
		setAction(actionUrl, task);
		mTaskScheduler.addTask(task);
		return task;
	}

	/**
	 * 设置Action到Task中
	 */
	private void setAction(String actionUrl, Task task) {
		String actionId = Task.parseActionId(actionUrl);
		Object obj = mActionContainer.get(actionId);// 从key的value
		if (obj == null) {
			throw new MVCException("在Action容器中没有找到该类->[" + actionId + "]");
		}
		if (Class.class.isInstance(obj)) {
			try {
				task.setAction((AbstractAction) Class.class.cast(obj).newInstance());
			} catch (Exception e) {
				Logger.error("实例化Action类出错", e);
			}
		} else {
			task.setAction((AbstractAction) obj);
		}
	}

}
