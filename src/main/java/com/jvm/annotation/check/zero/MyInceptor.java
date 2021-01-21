package com.jvm.annotation.check.zero;
//https://www.jianshu.com/p/0f0c9557c3cf
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod))
        {
            System.out.println("当前操作handler不为HandlerMethod=" + handler.getClass().getName()
                 + ",req="   + request.getQueryString());
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();

        UseCase useCase = handlerMethod.getMethod().getAnnotation(UseCase.class);
        if(useCase!=null){
            System.out.println("获取到注解了 " + methodName);
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
