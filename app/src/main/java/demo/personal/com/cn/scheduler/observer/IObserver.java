package demo.personal.com.cn.scheduler.observer;

/**
 * 观察者
 * 
 * @author RQHua
 *
 */
public interface IObserver {
	/**
	 * 更新界面
	 * 
	 * @param objects
	 */
	void updateView(Object... objects);
}
