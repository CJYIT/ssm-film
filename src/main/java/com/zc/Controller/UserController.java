package com.zc.Controller;

import com.google.code.kaptcha.Constants;
import com.zc.Service.UserService;
import com.zc.pojo.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制器
 */
@Controller
public class UserController {

    @Resource
    private HttpServletRequest request;
    @Autowired
    @Qualifier("userServiceimpl")
    private UserService userService;

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("/Login")
    public String getUserList(String username, String password, String code) {

        //查询数据库中的用户列表
        List<user> userList = userService.getUserList();
        for (user user : userList) {
            System.out.println(user);
            //判断是否有符合的
            if (user.getUsername().equals(username) && user.getPaw().equals(password)) {
                HttpSession session = request.getSession();
                Object attribute = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
                if (code.equals("111")) {
//                      model.addAttribute("user",user);
                    //request.setAttribute("user",user);
                    session.setAttribute("user", user);
                    return "main.jsp";
                } else {
                    request.setAttribute("mgs", "验证码错误");
                    return "index.jsp";
                }
            }

        }
        //  System.out.println(user.getUsername()+"-----"+user.getPaw());
        request.setAttribute("mgs", "用户名或密码错误");
        return "index.jsp";
    }

    /**
     * 退出登录接口
     *
     * @return
     */
    @RequestMapping("/exit")
    public String exit() {
        //删除session中的user，这样子拦截器就会拦截
        request.getSession().removeAttribute("user");
        return "index.jsp";
    }

    /**
     * 注册接口
     *
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String insertUser(user user) {
        List<user> userList = userService.getUserList();
        for (user user1 : userList) {
            if (user1.getUsername().equals(user.getUsername())) {
                request.setAttribute("mgs1", "已经存在该用户");
                return "index.jsp";
            }
        }
        System.out.println(user);
        userService.insertUser(user);
        return "index.jsp";
    }

    /**
     * 更新接口
     *
     * @param user
     * @return
     */
    @RequestMapping("/upUser")
    public String upUser(user user) {
        int i = userService.upUser(user);
        System.out.println(user + "-----" + i);
        if (i > 0) {
            user user1 = userService.getUserById(user.getId());
            request.getSession().setAttribute("user", user1);
            request.setAttribute("mgs4", "修改成功");
            return "person/person_info.jsp";
        } else {
            request.setAttribute("mgs4", "修改失败");
            return "person/person_info.jsp";
        }
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @RequestMapping("/uppaw")
    public String uppaw(user user, String paw1) {
        user userById = userService.getUserById(user.getId());
        System.out.println(user + "----------" + paw1);
        if (userById.getPaw().equals(paw1)) {
            userService.uppaw(user);
            user user1 = userService.getUserById(user.getId());
            request.getSession().setAttribute("user", user1);
            //md5加密+盐值用来密码加密
            request.setAttribute("mgs3", "修改密码成功");
            return "person/updatepwd.jsp";
        } else {
            request.setAttribute("mgs3", "输入原始密码不对");
            return "person/updatepwd.jsp";
        }
    }


}
