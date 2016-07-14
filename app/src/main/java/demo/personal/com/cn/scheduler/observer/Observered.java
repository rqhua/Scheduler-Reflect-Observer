package demo.personal.com.cn.scheduler.observer;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 被观察者
 * 
 * @author RQHua
 *
 */
public class Observered implements IObservered {
	private static Observered mObserved;
	private static CopyOnWriteArrayList<IObserver> list = new CopyOnWriteArrayList<IObserver>();

	private Observered() {
	}

	public static Observered getInstance() {
		if (mObserved == null)
			mObserved = new Observered();
		return mObserved;
	}

	public void addObserver(IObserver observer) {
		if (observer == null)
			return;
		// TODO 判重
		list.add(observer);
	}

	@Override
	public void notifyUpdateView(Object... objects) {
		Iterator<IObserver> iterator = list.iterator();
		while (iterator.hasNext()) {
			IObserver observer = iterator.next();
			observer.updateView(objects);
		}
	}

	public void removeAll() {
		list.clear();
	}
}
