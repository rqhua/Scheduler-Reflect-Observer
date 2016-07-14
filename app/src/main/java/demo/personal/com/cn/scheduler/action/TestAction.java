package demo.personal.com.cn.scheduler.action;

import java.util.Iterator;
import java.util.Map;

import demo.personal.com.cn.scheduler.action.base.AbstractAction;
import demo.personal.com.cn.scheduler.action.base.ActionCallback;
import demo.personal.com.cn.scheduler.util.Logger;


public class TestAction extends AbstractAction {

	public void testFun1(Map<String, Object> param, ActionCallback callback) {

		String value = new String();
		Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			value += key + ":" + param.get(key) + " \n";
		}
		value = "无返回值调用：\n" + value;
		//测试1
		callback.sendResponse(1,value);
		
		//测试2
//		final String result = value;
//		Looper l = Looper.getMainLooper();
//		Looper.prepare();
//		new Handler(l).postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				Observered.getInstance().notifyUpdateView(1, result);
//			}
//		}, 0);
		Logger.debug("testFun1 value : " + value);
	}

	public String testFun2(Map<String, Object> param, ActionCallback callback) {

		String value = new String();
		Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			value += key + ":" + param.get(key) + " \n";
		}
		return value;
	}
}
