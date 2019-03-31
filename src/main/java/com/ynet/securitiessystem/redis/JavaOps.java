package com.ynet.securitiessystem.redis;

import java.util.Set;

public class JavaOps {
	//根据推送产品获取用户更新
	public static Set<String> getLastByIdsAndUserPids(Set<String> productIds,Set<String> userPids){
		long startTime = System.currentTimeMillis();
		userPids.retainAll(productIds);
		System.out.println("交集："+userPids+",take "+(System.currentTimeMillis() - startTime)+" ms");
		return userPids;
	}
}
