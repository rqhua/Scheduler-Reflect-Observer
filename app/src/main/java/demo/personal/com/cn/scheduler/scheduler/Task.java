package demo.personal.com.cn.scheduler.scheduler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import demo.personal.com.cn.scheduler.action.base.AbstractAction;
import demo.personal.com.cn.scheduler.action.base.ActionCallback;
import demo.personal.com.cn.scheduler.util.BeanHelper;
import demo.personal.com.cn.scheduler.util.Logger;


/**
 * 任务类，执行指定的Action的方法、获取方法执行完之后的返回值，其生命周期仅为一次执行，使用线程锁机制实现， 任务执行完毕，解锁返回.
 */
public class Task implements Runnable {

	/**
	 * 要调用的Action类
	 */
	private AbstractAction action;
	/**
	 * 格式：action/methodName，（类名/方法名） 用于标记指定类的指定的方法
	 */
	private String actionUrl;
	/**
	 * 解析actionUrl得到的方法名
	 */
	private String methodName;
	/**
	 * 存放要执行的方法的参数，不包括ActionCallBack
	 */
	private Map<String, Object> param;
	private ActionCallback callback;
	/**
	 * 任务执行完后的返回值
	 */
	private Object result = null;

	/**
	 * 任务锁，
	 */
	private ReentrantLock resultLock = new ReentrantLock();
	private Condition resultCondition = resultLock.newCondition();

	private boolean hasReturn = false;

	@Override
	public void run() {
		if (action == null) {
			Logger.error("没有找到任务，请先初始化任务容器并且在xml文件中注册相应的任务类！");
			Logger.error("Not found action! Please initional ActionContain and register your Action Class");
			return;
		}
		if (callback == null) {
			Logger.warn("callback 不能为空！");
			Logger.warn("No call back");
		}
		try {
			resultLock.lock();
			action.doBefore(param, callback);
			invoke();
			action.doAfter(param, callback);
		} catch (Exception e) {
			String errorMsg = "Invoke method error: " + action.getClass().getName() + "#" + methodName;
			if (e.getCause() == null) {
				Logger.error(errorMsg, e);
			} else {
				if (e.getCause() instanceof UnknownHostException) {
					Logger.error(errorMsg);
					Logger.error(getStackTraceString(e.getCause()));
				} else {
					Logger.error(errorMsg, e.getCause());
				}
			}
		} finally {
			hasReturn = true;
			resultCondition.signalAll();
			resultLock.unlock();
		}
	}

	/**
	 * 同步取返回结果
	 */
	public Object getResult() {
		resultLock.lock();
		try {
			if (hasReturn == false) {
				resultCondition.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			resultLock.unlock();
		}
		return result;
	}

	/**
	 * 反射执行指定类的指定的方法
	 * 
	 * @throws Exception
	 */
	private void invoke() throws Exception {
		parseActionUrl();
		Class<?> callbackParam = ActionCallback.class;
		if (callback == null) {
		} else {
			callbackParam = callback.getClass().getSuperclass();
		}
		BeanHelper helper = new BeanHelper(action);
		Method method = helper.getMethod(methodName, Map.class, callbackParam);
		// 反射调用已经实例化的action对象中的method方法，param和callback是参数，参数个数不固定
		result = method.invoke(action, param, callback);
	}

	/**
	 * 解析类名
	 * 
	 * @param actionUrl
	 * @return
	 */
	public static String parseActionId(String actionUrl) {
		int index = actionUrl.indexOf("/");
		if (index == -1) {
			return actionUrl;
		}
		return actionUrl.substring(0, index);
	}

	/**
	 * 解析出方法名
	 */
	private void parseActionUrl() {
		int index = actionUrl.indexOf("/");
		if (index == -1) {
			methodName = "execute";
		} else {
			methodName = actionUrl.substring(index + 1);
		}
	}

	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * 设置方法参数，各个参数存放在Map集合里
	 * 
	 * @param param
	 *            Map
	 */
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	/**
	 * 设置ActionCallback，任务完成后，用ActionCallback进行发消息通知
	 * 
	 * @param callback
	 *            ActionCallback
	 */
	public void setCallback(ActionCallback callback) {
		this.callback = callback;
	}

	/**
	 * 设置方法标记参数
	 */
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	/**
	 * 设置Action
	 * 
	 * @param action
	 */
	public void setAction(AbstractAction action) {
		this.action = action;
	}

}
