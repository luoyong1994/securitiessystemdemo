package com.ynet.securitiessystem.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JavaOps {
	//根据推送产品获取用户更新
	public static Set<String> getLastByIdsAndUserPids(Set<String> productIds,Set<String> userPids){
		long startTime = System.currentTimeMillis();
		  Map<String,String> map = new HashMap<String,String>();
		  for(String s:productIds){
			  map.put(s, "1");
		  }
		  Map<String,String> map1 = new HashMap<String,String>();
		  for(String s:userPids){
			  map1.put(s, "1");
		  }
		Set<String> result = new HashSet<String>();
      for(String key : map1.keySet()){
      	if(map.get(key) != null){
      		result.add(key);
      	}
      }
		System.out.println(" sub "+result.size()+" take "+(System.currentTimeMillis() - startTime)+" ms， ");
		return result;

	}
}
