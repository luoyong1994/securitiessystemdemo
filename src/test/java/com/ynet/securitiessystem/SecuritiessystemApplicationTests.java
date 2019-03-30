package com.ynet.securitiessystem;

import com.ynet.securitiessystem.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecuritiessystemApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void name() {
        System.out.println("-----------");
        redisUtil.set("name","luoyong");
    }

    @Test
    public void contextLoads() {
        redisUtil.set("name","luoyong");
    }

}
