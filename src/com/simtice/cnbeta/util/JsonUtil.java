package com.simtice.cnbeta.util;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simtice.cnbeta.bean.NewsList;

public class JsonUtil {
	public static <T> List<T> parseBeanFromJson(String jsonData, Class<T> responseType){
		List<T> list = new ArrayList<T>();
		Type listType = new TypeToken<ArrayList<NewsList>>() {
		}.getType();
		Gson gson = new Gson();
		list = gson.fromJson(jsonData, listType);
		return list;
	}

}
