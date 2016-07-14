package demo.personal.com.cn.scheduler.action.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import demo.personal.com.cn.scheduler.util.Logger;


/**
 * Action容器，所有的action在其子类中初始化注入．
 */
public abstract class ActionContainer {

	protected Map<String, Object> actions = new HashMap<String, Object>();

	/**
	 * 初始化Action
	 */
	public abstract void initActions();

	public Map<String, Object> getActions() {
		return this.actions;
	}

	/**
	 * 查找clazz，存在返回，不存在则进行实例化
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private Object searchInstance(Class<? extends AbstractAction> clazz) throws Exception {
		for (Entry<String, Object> entry : actions.entrySet()) {
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}
			if (value.getClass().equals(clazz)) {
				return value;
			}
		}
		return clazz.newInstance();
	}

	/**
	 * 添加Action到ActionContainer，在添加Action时，会根据singleton的值来确定是否创建对象，确保对象的单例模式
	 * 
	 * @param actionId
	 *            Action类名
	 * @param clazz
	 *            Action类
	 * @param singleton
	 *            是否单例
	 * @return true 成功，false 失败。
	 */
	protected boolean addAction(String actionId, Class<? extends AbstractAction> clazz, boolean singleton) {
		if (singleton) {
			try {
				Logger.debug("add action :" + actionId);
				actions.put(actionId, searchInstance(clazz));
			} catch (Exception e) {
				Logger.error("创建单例失败！", e);
				return false;
			}
		} else {
			actions.put(actionId, clazz);
		}
		return true;
	}

	/**
	 * 每次都创建新的实例
	 * 
	 * @see #addAction(String, Class, boolean)
	 * @param actionId
	 *            Action类名
	 * @param clazz
	 *            Action类
	 * @return true 成功，false失败
	 */
	protected boolean addAction(String actionId, Class<? extends AbstractAction> clazz) {
		return addAction(actionId, clazz, false);
	}

	/**
	 * 添加Action到ActionContainer．在添加Action时，会根据singleton的值来确定是否创建对象，确保对象的单例模式.
	 * <br/>
	 * 
	 * @see #addAction(String, Class, boolean)
	 * @param clazz
	 *            Action类
	 * @param singleton
	 *            是否单例
	 * @return
	 */
	protected boolean addAction(Class<? extends AbstractAction> clazz, boolean singleton) {
		return addAction(clazz.getName(), clazz, singleton);
	}

	/**
	 * 每次都创建新的实例
	 * 
	 * @see #addAction(Class, boolean)
	 * @param clazz
	 *            action class
	 * @return
	 */
	protected boolean addAction(Class<? extends AbstractAction> clazz) {
		return addAction(clazz, false);
	}
}
