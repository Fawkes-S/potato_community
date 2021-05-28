package com.potato.advice.client.impl;

import com.potato.advice.client.UserClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * UserClient的失败处理类
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"Hystrix");
    }
}
