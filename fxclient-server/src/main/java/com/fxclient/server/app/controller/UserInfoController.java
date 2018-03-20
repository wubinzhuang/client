package com.fxclient.server.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.fxclient.server.app.model.PageData;
import com.fxclient.server.app.model.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "fxclient/userManager")
public class UserInfoController
{
    @ResponseBody
    @RequestMapping(value = "query.do", method = RequestMethod.POST)
    public PageData<UserInfo> doLogin(HttpServletRequest request)
    {
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String userName = request.getParameter("userName");
        String empName = request.getParameter("empName");
        String empCode = request.getParameter("empCode");
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < pageSize; i++)
        {
            int no = pageSize * (page - 1) + i + 1;
            UserInfo vo = new UserInfo();
            if (empCode != null && !"".equals(empCode))
            {
                vo.setEmpCode(empCode + no);
            }
            else
            {
                vo.setEmpCode("USER" + no);
            }
            if (empName != null && !"".equals(empName))
            {
                vo.setEmpName(empName + no);
            }
            else
            {
                vo.setEmpName("用户" + no);
            }
            if (userName != null && !"".equals(userName))
            {
                vo.setUserName(userName + no);
            }
            else
            {
                vo.setUserName("user" + no);
            }
            vo.setId("EMP" + no);
            vo.setSex(new Random().nextBoolean() ? "男" : "女");
            vo.setTel(new Random().nextInt(1000) + "");
            list.add(vo);
        }
        PageData<UserInfo> pageData = new PageData<>();
        pageData.setPage(page);
        pageData.setPageSize(pageSize);
        pageData.setTotlePage(10);
        pageData.setTotleRecords(95);
        ;
        pageData.setRecords(list);
        return pageData;
    }
    
    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public String addOrUpdate(HttpServletRequest request)
    {
        String id = request.getParameter("id");
        String empName = request.getParameter("empName");
        String userName = request.getParameter("userName");
        String tel = request.getParameter("tel");
        String empCode = request.getParameter("empCode");
        String sex = request.getParameter("sex");
        if ("admin".equals(userName)) { return "该用户已存在"; }
        return "OK";
    }
    
    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws InterruptedException
    {
        String id = request.getParameter("id");
        Thread.sleep(1000);
        return "OK";
    }
    
    @ResponseBody
    @RequestMapping(value = "export.do", method = RequestMethod.GET)
    public List<UserInfo> export(HttpServletRequest request)
    {
        // int page = Integer.parseInt(request.getParameter("page"));
        // int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        // String userName = request.getParameter("userName");
        // String empName = request.getParameter("empName");
        // String empCode = request.getParameter("empCode");
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            int no = i + 1;
            UserInfo vo = new UserInfo();
            vo.setEmpCode("USER" + no);
            vo.setEmpName("用户" + no);
            vo.setUserName("user" + no);
            vo.setId("EMP" + no);
            vo.setSex(new Random().nextBoolean() ? "男" : "女");
            vo.setTel(new Random().nextInt(1000) + "");
            list.add(vo);
        }
        return list;
    }
}
