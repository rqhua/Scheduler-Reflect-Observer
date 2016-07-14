package demo.personal.com.cn.scheduler;


import android.os.Handler;
import android.os.Message;

import demo.personal.com.cn.scheduler.action.base.ActionCallback;

public class ImActionCallback extends ActionCallback {

	private Handler handler;

	public ImActionCallback(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void sendResponse(int what) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	@Override
	public void sendResponse(int what, int arg1) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}

	@Override
	public void sendResponse(int what, int arg1, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	@Override
	public void sendResponse(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

}
