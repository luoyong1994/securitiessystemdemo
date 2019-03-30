package com.ynet.securitiessystem.nettyserve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ynet.securitiessystem.model.Bond;
import com.ynet.securitiessystem.redis.RedisUtil;

@Component
public class RedisService {
    @Autowired
    private RedisUtil redisUtil;
    
    public void setProductIdByJsonData(String key,String keyId,String jsonData){
        List<Bond> result = JSON.parseArray(jsonData, Bond.class);
        Map<String, Object> map = new HashMap<String,Object>();
        String[] strIdArray = new String[result.size()];
        for(int i=0 ; i < result.size(); i++){        
        	strIdArray[i] = result.get(i).getSymbol();
        	map.put(strIdArray[i], result.get(i));        	
        }  
        redisUtil.hmset(keyId, map);
        
        redisUtil.sSet(keyId, strIdArray);
    }
    
    public Set<Object> getJsonObject(String key){
    	return redisUtil.sGet(key);
    }
    

}
