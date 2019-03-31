package com.ynet.securitiessystem.controller;


import com.alibaba.fastjson.JSON;
import com.ynet.securitiessystem.model.Bond;
import com.ynet.securitiessystem.model.Group;
import com.ynet.securitiessystem.model.User;
import com.ynet.securitiessystem.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
@ResponseBody
@RequestMapping("/data/")
public class GroupController {
	
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("createGroup.do")
    public String createGroup(HttpServletRequest request,String data){
        Map<String,Object> groupData = JSON.parseObject(data,Map.class);
        HttpSession httpSession = request.getSession();
        String groupName = (String) groupData.get("groupName");//获取用户创建的组名
        String userId = (String) groupData.get("userId");
        String groupKey = UUID.randomUUID().toString().replaceAll("-","");//goupId
        Map<String, Object> map = new HashMap<String,Object>();
        Group group = new Group();
        group.setGroupName(groupName);
        List list = (List) groupData.get("productList");
        Set<Bond> set = new HashSet<Bond>();
        for(int i=0;i<list.size();i++){
            Bond bond = JSON.parseObject(list.get(i).toString(),Bond.class);
            set.add(bond);
        }
        group.setProductList(set);
        User user = new User();
        user.setUserId(userId);
        
        group.setUser(user);
        map.put(groupKey,group);
        redisUtil.hmset(userId, map);
        return groupKey;
    }
    
    @RequestMapping("showGroup.do")
    public Map showGroup(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
    	String userId = httpSession.getId();//获取用户id
    	Map<Object,Object> map = redisUtil.hmget(userId);
    	return map;
    }

    @PostMapping("selectOneGroup.do")
    public String selectOneGroup(HttpServletRequest request,@RequestParam("groupKey") String groupKey,@RequestParam("userId") String userId){
        redisUtil.set(userId+"_groupKey",groupKey);
        System.out.println(groupKey);
        HttpSession httpSession = request.getSession();
//    	String userId = httpSession.getId();
    	Map<Object,Object> map = redisUtil.hmget(userId);
    	Group group = (Group) map.get(groupKey);
        System.out.println(group);
    	return "success";
    }

}
