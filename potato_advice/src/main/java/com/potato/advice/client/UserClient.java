package com.potato.advice.client;

import com.potato.advice.client.impl.UserClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "potato-user", fallback = UserClientImpl.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    public Result findById(@PathVariable String id);
}
