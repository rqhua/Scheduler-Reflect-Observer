package demo.personal.com.cn.scheduler;


import android.app.Application;

import demo.personal.com.cn.scheduler.action.ImActionContainer;
import demo.personal.com.cn.scheduler.scheduler.TaskManager;

public class App extends Application {

	public static String model = "ACTION";

	@Override
	public void onCreate() {
		super.onCreate();

		TaskManager.initActionContainer(new ImActionContainer(model, getApplicationContext()));
	}

}
