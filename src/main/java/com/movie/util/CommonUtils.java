package com.movie.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * common util 클래스.
 *
 * @author onion
 *
 */
public class CommonUtils {

	/**
	 * 맵 객체 키와 동일한 필드에 데이터 바인딩.
	 *
	 * @param data
	 *            맵 타입 데이터.
	 * @param obj
	 *            맵 객체의 데이터 바인딩되는 오브젝트.
	 * @return obj 객체.
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object mapToClassObject(Map<String, Object> data, Object obj)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String keyAttribute = null;
		String setMethodString = "set";
		String methodString = null;
		Iterator<String> itr = data.keySet().iterator();

		while (itr.hasNext()) {
			keyAttribute = (String) itr.next();
			methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
			Method[] methods = obj.getClass().getDeclaredMethods();

			for (int i = 0; i < methods.length; i++) {
				if (methodString.equals(methods[i].getName())) {
					methods[i].invoke(obj, data.get(keyAttribute));
				}
			}
		}
		return obj;
	}

	/**
	 * MessageVo 데이터를 json 형태로 변환.
	 *
	 * @param <T>
	 *            메시지 객체 타입.
	 * @param messageVo
	 *            메시지 객체.
	 * @return json 타입 문자열.
	 * @throws JsonProcessingException
	 */
	public static <T> String messageVoToJson(T messageVo) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		// 매핑되지 않는 필드 무시.
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.writeValueAsString(messageVo);
	}

	/**
	 * json 형태 데이터를 MessageVo로 변환.
	 *
	 * @param <T>
	 *            메시지 객체 타입.
	 * @param jsonString
	 *            json 형태 데이터.
	 * @param messageVoClass
	 *            메시지 클래스.
	 * @return 메시지 객체.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static <T> T jsonToMessageVo(String jsonString, Class<T> messageVoClass)
			throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		// 매핑되지 않는 필드 무시.
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.readValue(jsonString, messageVoClass);
	}
}
