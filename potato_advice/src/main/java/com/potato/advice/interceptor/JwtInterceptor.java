package com.potato.advice.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 签权的拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private int i = 0;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        System.out.println("*Through the interceptor(JWT)*");
        //有header的，把里面的token令牌解析验证
        String header = request.getHeader("Authorization");
        System.out.println("Header###"+header);
        if(header!=null && !"".equals(header)){
            if(header.startsWith("Bearer")){
                String token = header.substring(7);System.out.println("Token###"+token);
                i++;
                System.out.println("------"+i+"------");
                if(i>1){
                    //对令牌认证
                    try {
                        Claims claims = jwtUtil.parseJWT(token);
                        System.out.println("claims::"+claims);
                        String roles = (String)claims.get("roles");
                        System.out.println("roles::"+roles);
                        if(roles!=null){
                            if(roles.equals("admin")) {
                                request.setAttribute("claims_admin",token);
                            }
                            if(roles.equals("user")) {
                                request.setAttribute("claims_user",token);
                            }
                            i = 0;
                            System.out.println("$$$$"+i+"------");
                        }
                    }catch(Exception e){
                        throw new RuntimeException("token不正确！");
                    }

                }

            }

        }
        return true;//true放行
    }

}
