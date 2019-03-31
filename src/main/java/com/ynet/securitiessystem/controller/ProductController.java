package com.ynet.securitiessystem.controller;



import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynet.securitiessystem.model.Bond;
import com.ynet.securitiessystem.model.Group;
import com.ynet.securitiessystem.nettyserve.RedisService;
import com.ynet.securitiessystem.redis.RedisUtil;



@Controller
@ResponseBody
public class ProductController {
	
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("subProduct.do")
    public String subProduct(){
        String groupKey = "";//goupId
        String userId = "";//获取用户id
        String symbol = "";//hu
        String key ="";//chan
    	Map<Object,Object> map = redisUtil.hmget(userId);
    	Group group = (Group) map.get(groupKey);
    	Set<Bond> productList = group.getProductList();
    	redisUtil.hmget(key);
        return "success";
    }
 



}
