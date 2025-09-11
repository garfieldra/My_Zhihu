package com.example.myzhihu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
public class RedisIntegrationTest {

    private static final String TEST_KEY = "test";


    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp(){
        redisTemplate.delete(TEST_KEY);
    }

    @AfterEach
    void tearDown(){
        redisTemplate.delete(TEST_KEY);
    }

    @Test
    void testSetAndGetString()
    {
        redisTemplate.opsForValue().set(TEST_KEY,"hello-redis", Duration.ofMinutes(10));

        String value = redisTemplate.opsForValue().get(TEST_KEY);
        Assertions.assertEquals("hello-redis", value, "从Redis读出的值应该和写入的一样");
    }

    @Test
    void TestIncrementCounter()
    {
        String cntKey = "unittest:redis:counter";

        redisTemplate.delete(cntKey);

        Long after = redisTemplate.opsForValue().increment(cntKey);
        Assertions.assertEquals(1L, after);

        String got = redisTemplate.opsForValue().get(cntKey);
        Assertions.assertEquals(1L, after);

        redisTemplate.delete(cntKey);

    }


}
