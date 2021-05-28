package com.potato.gathering.client.impl;

import com.potato.gathering.client.UserClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * UserClient的失败处理类
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public Result addGathering(Map<String, String> map) {
        return new Result(false, StatusCode.ERROR,"Hystrix");
    }

    @Override
    public Result findAll() {
        return new Result(false, StatusCode.ERROR,"Hystrix");
    }

    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"Hystrix");
    }


}
