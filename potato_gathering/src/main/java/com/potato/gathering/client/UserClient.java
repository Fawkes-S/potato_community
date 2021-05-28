package com.potato.gathering.client;

import com.potato.gathering.client.impl.UserClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "potato-user", fallback = UserClientImpl.class)
public interface UserClient {
    @PostMapping("/user/addGathering")
    public Result addGathering(@RequestBody Map<String,String> map);

    @GetMapping("/user")
    public Result findAll();

    @GetMapping("/user/{id}")
    public Result findById(@PathVariable String id);
}
