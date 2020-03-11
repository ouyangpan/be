package com.be.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

	public static List<String> getNewListDiffInOldList(List<String> oldList,List<String> newList){
		List<String> result = new ArrayList<>();
		Map map = new HashMap<>(oldList.size());
		for (int i = 0; i < oldList.size(); i++) {
			map.put(oldList.get(i), null);
		}
		
		for (int i = 0; i < newList.size(); i++) {
			if(!map.containsKey(newList.get(i))) {
				result.add(newList.get(i));
			}
		}
		
		return result;
	}
}
