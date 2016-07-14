package demo.personal.com.cn.scheduler.util;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import demo.personal.com.cn.scheduler.R;


public class ActionReflect {

	/**
	 * 反射查找属性名和属性值
	 * 
	 * @param obj--要查找的类的实例，model--要查找的属性名
	 * @return 属性值--0表示没查找到model对应的属性名
	 */
	public static int getFields(Object obj, String model) {

		int stringId = 0;
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			if (model.equals(field.getName())) {
				try {
					stringId = field.getInt(obj);
					return stringId;
				} catch (IllegalAccessException e1) {
				} catch (IllegalArgumentException e2) {
				}
			}
		}
		return stringId;
	}

	/**
	 * 获取xml字符串，并将其分割为一个字符串集合（字符串用“，”隔开）
	 * 
	 * @param con
	 * @param name
	 * @return
	 */
	public static List<String> getStringsXml(Context con, String name) {
		R.string strs = new R.string();
		int stringId = getFields(strs, name); // 找到该设备在R文件中存放的位置，从而获取目录等信息
		List<String> array = new ArrayList<String>();
		try {
			String temp = con.getString(stringId);
			String[] strs1 = spiltStrings(temp, ",");
			for (String string : strs1) {
				array.add(string);
			}
		} catch (Exception e) {
			array = null;
		}
		return array;
	}

	/**
	 * 获取xml中dArray数组
	 * 
	 * @param con
	 * @param arrayName
	 * @return
	 */
	public static List<String> getArraysXml(Context con, String arrayName) {
		R.array strs = new R.array();
		int stringId = getFields(strs, arrayName); // 找到该设备在R文件中存放的位置，从而获取目录等信息
		List<String> array = new ArrayList<String>();
		try {
			String[] classItems = con.getResources().getStringArray(stringId);
			for (String classItem : classItems) {
				array.add(classItem);
			}
		} catch (Exception e) {
			array = null;
		}
		return array;
	}

	/**
	 * 分割字符串
	 * 
	 * @param str
	 * @param reg
	 *            分割标记
	 * @return
	 */
	public static String[] spiltStrings(String str, String reg) {
		String[] arrayStr = str.split(reg);
		return arrayStr;
	}

}
