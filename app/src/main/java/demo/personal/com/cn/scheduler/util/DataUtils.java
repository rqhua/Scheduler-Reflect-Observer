package demo.personal.com.cn.scheduler.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DataUtils {
	private static List<String> keys;
	private static Map<String, Object> map;

	/**
	 * 将value值加入Map，要先通过setParamKey设置key，并且顺序对应
	 * 
	 * @param callback
	 * @param params
	 * @return
	 */
	public static void setParamsValue(Object... params) {
		List<String> keys = getParamKey();
		if (keys == null || keys.size() <= 0) {
			Logger.error("key集合为空！！！");
			return;
		}
		if (keys.size() != params.length) {
			Logger.error("key-value参数对应设置有误！！！");
			return;
		}
		map = new HashMap<String, Object>();
		for (int i = 0; i < keys.size(); i++) {
			map.put(keys.get(i), params[i]);
		}
	}

	/**
	 * 获取存放函数参数的集合
	 * 
	 * @return
	 */
	public static Map<String, Object> getParams() {
		return map;
	}

	/**
	 * 设置Map参数的key集合
	 * 
	 * @param paramsKey
	 * @return
	 */
	public static void setParamKey(String... paramsKey) {

		if (paramsKey == null || paramsKey.length <= 0)
			return;
		keys = new Vector<String>();
		for (String string : paramsKey) {
			keys.add(string);
		}
	}

	private static List<String> getParamKey() {
		return keys;
	}
}
