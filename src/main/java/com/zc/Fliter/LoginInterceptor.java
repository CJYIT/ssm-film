package com.zc.Fliter;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 拦截器
 * @author MoYu-zc
 * @date 2020/12/23 23:34
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {

        System.out.println("uri: " + request.getRequestURI());
        //对于一些静态文件，比较js、css等文件不拦截，避免前端页面展示资源未找到
        if (request.getRequestURI().contains(".js")||request.getRequestURI().contains(".css")
                ||request.getRequestURI().contains(".png")||request.getRequestURI().contains(".jpg")) {
            return true;
        }
        HttpSession session = request.getSession();

        //如果session中有user表示用户已经登录了，那么就不拦截
        if (session.getAttribute("user")!= null) {
            return true;
        }

        //未登录的用户就进行跳转到登录界面
        request.getRequestDispatcher(request.getContextPath()+"/index.jsp").forward(request,response);
        return false;
}

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}

