package demo.personal.com.cn.scheduler.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BeanHelper {
	/**
	 * 要反射的类
	 */
	private Class<?> mClass;
	private Object mObject;
	/**
	 * 类中声明的方法
	 */
	private Method[] declaredMethods;

	public BeanHelper(Class<?> clazz) {
		mClass = clazz;
	}

	public BeanHelper(Object obj) {
		mObject = obj;
		mClass = mObject.getClass();
	}

	/**
	 * 得到指定的方法
	 * 
	 * @param methodName 要找的方法名
	 * @param classes methodName的参数类型，不定个数
	 * @return
	 * @throws NoSuchMethodException
	 */
	public Method getMethod(String methodName, Class<?>... classes) throws NoSuchMethodException {
		declaredMethods = mClass.getDeclaredMethods();
		Method result = null;
		int matchLevel = -1;
		boolean isFirst = true;
		for (Method method : declaredMethods) {
			String name = method.getName();
			// 过滤掉不匹配的方法名
			if (!name.equals(methodName)) {
				continue;
			}
			//初步匹配到的方法的参数类型数组
			Class<?>[] paramTypes = method.getParameterTypes();
			// 参数个数是否匹配
			if (paramTypes.length != classes.length) { 
				continue;
			}
			//计算匹配程度
			int tempMatchLevel = matchLevel(paramTypes, classes);
			if (tempMatchLevel < 0) {
				continue;
			}
			//找到参数个数匹配并且匹配度最高的方法
			if (isFirst && matchLevel < tempMatchLevel) {
				isFirst = false;
				matchLevel = tempMatchLevel;
			} else if (matchLevel < tempMatchLevel) { 
				matchLevel = tempMatchLevel;
			} else {
				continue;
			}
			result = method;
		}
		if (result == null) {
			throw new NoSuchMethodException(methodName + " " + Arrays.asList(classes).toString() + "");
		}
		return result;
	}

	public Class<?> getClosestClass(Class<?> clazz) {
		return clazz.getSuperclass();
	}

	/**
	 * 计算方法的匹配程度，用整数进行表示匹配度 calculate the method matching degree. which is
	 * represent with int level.It's important
	 * 
	 * @param paramTypes
	 *            方法的所有的参数的类型数组
	 * @param destParamTypes
	 *            目标方法的所有参数的类型数组
	 * @return metch level. 匹配度，1位匹配度最高，返回值越大，匹配度越低 begin from 1. 1 is th
	 *         highest level. the level number is bigger, matching degree is
	 *         lower.
	 * 
	 */
	public int matchLevel(Class<?>[] paramTypes, Class<?>[] transferParamTypes) {
		int matchLevel = -1;
		for (int m = 0; m < paramTypes.length; m++) {
			Class<?> paramType = paramTypes[m];
			Class<?> tParamType = transferParamTypes[m];
			if (paramType.equals(tParamType)) {
				matchLevel += 1;
				continue;
			}
			List<Class<?>> superClasses = getAllSuperClass(tParamType);
			for (int n = 1; n <= superClasses.size(); n++) {
				Class<?> superClass = superClasses.get(n - 1);
				if (superClass == null || superClass.equals(paramType)) {
					matchLevel += n;
				} else {
					break;
				}
			}
		}
		return matchLevel;
	}

	/**
	 * 获取指定类clazz的所有的父类
	 * 
	 * @param 指定的类clazz
	 * @return clazz父类的集合
	 */
	public static List<Class<?>> getAllSuperClass(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> cla = clazz;
		do {
			cla = cla.getSuperclass();
			Logger.debug("class: " + clazz + ", super class: " + cla);
			if (cla != null) {
				classes.add(cla);
			}
			if (cla != null && cla.equals(Object.class)) {
				break;
			}
		} while (cla != null);
		return classes;
	}
}
