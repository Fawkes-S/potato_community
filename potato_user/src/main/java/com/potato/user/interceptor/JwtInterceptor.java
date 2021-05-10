package com.potato.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 签权的拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        System.out.println("*Through the interceptor*");
        //有header的，把里面的token令牌解析验证
        String header = request.getHeader("Authorization");

        if(header!=null && !"".equals(header)){

            if(header.startsWith("Bearer ")){
                String token = header.substring(7);
                //对令牌认证
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String)claims.get("roles");
                    if(roles!=null&&roles.equals("admin")) {
                        request.setAttribute("claims_admin",token);
                    }
                    if(roles!=null&&roles.equals("user")) {
                        request.setAttribute("claims_user",token);
                    }
                }catch(Exception e){
                        throw new RuntimeException("token不正确！");
                    }
            }

        }
        return true;//true放行
    }

}
