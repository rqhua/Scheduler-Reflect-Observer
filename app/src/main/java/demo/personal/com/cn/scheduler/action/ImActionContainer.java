package demo.personal.com.cn.scheduler.action;

import android.content.Context;

import java.util.List;

import demo.personal.com.cn.scheduler.R;
import demo.personal.com.cn.scheduler.action.base.ActionContainer;
import demo.personal.com.cn.scheduler.util.ActionReflect;
import demo.personal.com.cn.scheduler.util.Logger;

public class ImActionContainer extends ActionContainer {

	private String model;
	private Context context;

	public ImActionContainer(String model, Context context) {
		this.model = model;
		this.context = context;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initActions() {

		Logger.debug("Model = " + model);
		List<String> classItems = ActionReflect.getArraysXml(context, model);
		if (classItems != null) {
			Logger.debug("Actions " + classItems.toString());
		}

		if (classItems == null) {
			classItems = ActionReflect.getArraysXml(context, "ACTION");
			Logger.debug("Actions " + classItems.toString());
		}

		for (int i = 0; i < classItems.size(); i++) {
			try {
				/**
				 * Class<? extends AbstractAction>
				 */
				Class clazz = Class
						.forName(context.getResources().getString(R.string.action_package_name) + classItems.get(i));
				addAction(classItems.get(i), clazz, true);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("找不到Action");
			}
		}
	}

}
