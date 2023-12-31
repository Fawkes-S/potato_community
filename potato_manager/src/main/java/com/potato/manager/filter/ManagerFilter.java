package com.potato.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台zuul过滤器
 */
@Component
public class ManagerFilter extends ZuulFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

//        //1.获取请求对象
//        RequestContext currentContext = RequestContext.getCurrentContext();
//
//        HttpServletRequest request = currentContext.getRequest();
//
//        if(request.getMethod().equals("OPTIONS")){
//            return null;
//        }
//
//        //单独放行管理员登录请求: /admin/login
//        String uri = request.getRequestURI();
//
//        if(uri.indexOf("/admin/login")>0){
//
//            //直接放行
//            return null;
//        }
//
//        //2.获取验证信息
//
//        String auth = request.getHeader("Authorization");
//
//        //3.判断验证信息是否符合基本规则
//
//        if(auth!=null && auth.startsWith("Bearer ")){
//
//            //4.截取token
//            String token = auth.substring(7);
//            try{
//                //5.校验token是否合法
//                Claims claims = jwtUtil.parseJWT(token);
//                String roles = (String)claims.get("roles");
//                //6.判断是否管理员身份登录
//                if(claims!=null && roles.equals("admin")){
//                    //header信息转发下去并放行
//                    currentContext.addZuulRequestHeader("Authorization",auth);
//                    return null;
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//                currentContext.setSendZuulResponse(false);//终止运行
//            }
//
//
//        }
//
//        //终止请求
//        //返回提示信息
//        //设置返回状态码
//        currentContext.setResponseStatusCode(403);  //403无权访问
//
//        //设置输出内容
//        currentContext.setResponseBody("你不是管理员,无权访问");
//
//        //设置输出内容的编码
//        currentContext.getResponse().setContentType("text/html;charset=utf-8");
//
//        currentContext.setSendZuulResponse(false);

        return null;
    }
}