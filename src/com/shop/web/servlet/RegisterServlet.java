package com.shop.web.servlet;

import com.shop.domain.User;
import com.shop.server.UserService;
import com.shop.until.CommonUtils;
import com.shop.until.MailUtils;
import com.sun.org.apache.bcel.internal.generic.POP;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Map<String, String[]>  populates=request.getParameterMap();
        User user=new User();
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class aClass, Object o) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyy-MM-dd");
                Date parse=null;
                try {
                    parse=simpleDateFormat.parse(o.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return parse;
            }
        }, Date.class);
        try {
            BeanUtils.populate(user,populates);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService=new UserService();
//        设置id
        String id=CommonUtils.getId();
        user.setUid(id);
//        设置电话
        user.setTelephone("");
//        设置状态
        user.setState(0);
//        设置code
        String activeCode=CommonUtils.getId();
        user.setCode(activeCode);
        System.out.println(user);
         Boolean register=userService.register(user);

        if(register){
            String emailMsg="恭喜您注册成功，请点击下面的连接进行激活账户"
                    + "<a href='http://localhost:8080/shop/active?activeCode="+activeCode+"'>"
                    + "http://localhost:8080/shop/active?activeCode="+activeCode+"</a>";;
            try {
                MailUtils.sendMail(user.getEmail(),emailMsg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");

        }else{
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }
}
