package com.Jay.androidClient;

import java.util.ArrayList;
import java.util.List;

import com.Jay.entity.Student;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	public static String bean2Json(Object bean) {
		StringBuffer sb = new StringBuffer();
		JSONObject json = JSONObject.fromObject(bean);
		return json.toString();
	}

	public static <T> T json2Bean(String jsonString, Class<T> beanCalss) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		T bean = (T) JSONObject.toBean(jsonObject, beanCalss);
		return bean;
	}

	public static String beanList2JsonList(List<?> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}

	public static <T> List<T> jsonList2BeanList(String jsonString, Class<T> beanClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		T bean;
		int size = jsonArray.size();
		List<T> list = new ArrayList<T>(size);
		for (int i = 0; i < size; i++) {

			jsonObject = jsonArray.getJSONObject(i);
			bean = (T) JSONObject.toBean(jsonObject, beanClass);
			list.add(bean);
		}
		return list;
	}
}
