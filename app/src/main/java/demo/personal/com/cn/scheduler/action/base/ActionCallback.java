package demo.personal.com.cn.scheduler.action.base;

import android.content.Context;

/**
 * 回调方法，允许扩展，适应实际需求.其中的方法主要用于发送消息，其子类中加入Handler 在Action类中调用其子类重写的方法进行夸线程通讯
 */
public abstract class ActionCallback {
	protected Context context;

	public ActionCallback() {
	}

	public Context getContext() {
		return this.getContext();
	}

	// 一下为子类重写后发消息的类,
	public void sendResponse(int what) {
	}

	public void sendResponse(int what, int arg1) {
	}

	public void sendResponse(int what, int arg1, Object obj) {
	}

	public void sendResponse(int what, Object obj) {
	}
}
