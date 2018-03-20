package com.fxclient.server.app.controller;

import javax.servlet.http.HttpServletRequest;

import com.fxclient.server.app.model.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "fxclient/login")
public class LoginController
{
    @ResponseBody
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public UserInfo doLogin(HttpServletRequest request)
    {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        UserInfo v = new UserInfo();
        if ("admin".equals(userName) && "202cb962ac59075b964b07152d234b70".equals(password))
        {
            v.setUserName(userName);
            v.setPassword(password);
        }
        return v;
    }
    
    @ResponseBody
    @RequestMapping(value = "test.do", method = RequestMethod.GET)
    public String test(HttpServletRequest request)
    {
        return "OK";
    }
}
