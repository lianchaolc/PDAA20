package cn.poka.util;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 操作SharedPreference对象的工具类
 * 
 * @ClassName: SharedPreUtil
 * @Description: TODO xxxxxxxx
 * @author liuchang
 * @date 2017-4-26 下午3:44:07
 */
public class SharedPreUtil {
	/**
	 * SharedPreference中存储用户名的KEYNAME
	 */
	private static final String KEY_NAME = "KEY_NAME";

	private static SharedPreferences sharedPre;

	private static SharedPreUtil sharePreUtil;

	/**
	 * 私有构造器
	 * 
	 * @param context
	 */
	private SharedPreUtil(Context context) {
		sharedPre = context.getSharedPreferences("SharedPreUtil", Context.MODE_PRIVATE | Context.MODE_APPEND);
	}

	/**
	 * 
	 * @Title: initSharedPreferences
	 * @Description: TODO SharedPreUtil工具类初始化方法
	 * @param context
	 * @author liuchang
	 * @date 2017-4-26 下午3:29:12
	 */
	public synchronized static void initSharedPreferences(Context context) {
		if (sharePreUtil == null) {
			sharePreUtil = new SharedPreUtil(context);
		}
	}

	/**
	 * 
	 * @Title: getInstance
	 * @Description: TODO 单例模式
	 * @return
	 * @author liuchang
	 * @date 2017-4-26 下午3:42:44
	 */
	public synchronized static SharedPreUtil getInstance() {
		return sharePreUtil;
	}

	/**
	 * @Title: putUserName
	 * @Description: TODO 保存用户名到SharedPreference
	 * @param username
	 * @author liuchang
	 * @date 2017-4-26 下午3:42:58
	 */
	public synchronized static void putUserName(String username) {
		// Editor必须调用一次clear()方法，否则重新进入app后set中保存数据失败
		Editor editor = sharedPre.edit().clear();
		Set<String> nameSet = new HashSet<String>();
		nameSet = sharedPre.getStringSet(KEY_NAME, nameSet);
		nameSet.add(username);

		editor.putStringSet(KEY_NAME, nameSet);
		editor.commit();
	}

	/**
	 * 
	 * @Title: getUserArray
	 * @Description: TODO 获取指定KEY名称的Value值（Set类型），拼装成数组返回调用者
	 * @return
	 * @author liuchang
	 * @date 2017-4-26 下午5:31:22
	 */
	public synchronized static String[] getUserArray() {
		Set<String> nameSet = new HashSet<String>();
		nameSet = sharedPre.getStringSet(KEY_NAME, nameSet);
		String[] paramArray = new String[nameSet.size()];
		String[] userArray = (String[]) nameSet.toArray(paramArray);
		return userArray;
	}
}
