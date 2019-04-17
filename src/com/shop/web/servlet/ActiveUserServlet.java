package com.shop.web.servlet;

import com.shop.server.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "ActiveUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code=request.getParameter("activeCode");
        UserService userService=new UserService();
        Boolean isActive=userService.active(code);
        if(isActive){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }else{
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }
}
