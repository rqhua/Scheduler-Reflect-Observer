package demo.personal.com.cn.scheduler;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import demo.personal.com.cn.scheduler.action.manager.TextActionManager;
import demo.personal.com.cn.scheduler.observer.IObserver;
import demo.personal.com.cn.scheduler.observer.Observered;

public class MainActivity extends Activity implements IObserver {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Observered.getInstance().addObserver(this);
	}

	public void onClick(View v) {
		TextActionManager.getInstance().text1();
		TextActionManager.getInstance().text2();
	}

	@Override
	public void updateView(Object... objects) {
		int type = (Integer) objects[0];
		String result = (String) objects[1];
		if (type == 1)
			((TextView) findViewById(R.id.text1)).setText(result);
		if (type == 2)
			((TextView) findViewById(R.id.text2)).setText(result);
	}

}
