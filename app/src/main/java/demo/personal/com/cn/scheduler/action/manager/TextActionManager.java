package demo.personal.com.cn.scheduler.action.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import demo.personal.com.cn.scheduler.ImActionCallback;
import demo.personal.com.cn.scheduler.action.base.ActionCallback;
import demo.personal.com.cn.scheduler.observer.Observered;
import demo.personal.com.cn.scheduler.scheduler.TaskManager;
import demo.personal.com.cn.scheduler.util.DataUtils;
import demo.personal.com.cn.scheduler.util.Logger;

public class TextActionManager {
	/**
	 * callback 用于通讯，
	 */
	private ActionCallback callback;
	private static TextActionManager mManager;

	private TextActionManager() {
		Looper l = Looper.getMainLooper();
		callback = new ImActionCallback(new Handler(l) {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1)
					Observered.getInstance().notifyUpdateView(1, (String) msg.obj);
				if (msg.what == 2)
					Observered.getInstance().notifyUpdateView(2, (String) msg.obj);

				super.handleMessage(msg);
			}
		});
	}

	public static TextActionManager getInstance() {
		if (mManager == null)
			mManager = new TextActionManager();
		return mManager;
	}

	public static final String textFun1 = "TestAction/testFun1";
	public static final String textFun2 = "TestAction/testFun2";

	// Demo没有涉及callback使用，callback实际是一个发消息工具
	public void text1() {
		//调用的方法的参数放在map里
		DataUtils.setParamKey("id", "name", "gender");
		DataUtils.setParamsValue("1", "李四", "女");
		TaskManager.doSubmit(textFun1, DataUtils.getParams(), callback);
	}

	public void text2() {
		DataUtils.setParamKey("id", "name", "gender");
		DataUtils.setParamsValue("123", "张三", "男");
		final String result = "带返回值调用：\n" + TaskManager.doSubmitForResult(textFun2, DataUtils.getParams(), callback);
		//测试显示1
		callback.sendResponse(2, result);
		//测试显示2
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Observered.getInstance().notifyUpdateView(2, result);
//			}
//		}, 0);
		Logger.debug("textFun2 result : " + result);
	}
}
