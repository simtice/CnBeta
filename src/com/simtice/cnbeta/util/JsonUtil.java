package com.simtice.cnbeta.util;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class JsonUtil {
	public static <T> List<T> parseBeanFromJson(String jsonData, Type listType) throws Exception{
		List<T> list = new ArrayList<T>();
		Gson gson = new Gson();
		list = gson.fromJson(jsonData, listType);
		return list;
	}

}
