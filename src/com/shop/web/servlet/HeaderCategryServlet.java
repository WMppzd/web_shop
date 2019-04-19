package com.shop.web.servlet;

import com.google.gson.Gson;
import com.shop.domain.Category;
import com.shop.server.IndexService;
import com.shop.until.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.List;

//@WebServlet(name = "HeaderCategryServlet")
public class HeaderCategryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Jedis jedis = JedisPoolUtils.getJedis();
        String result=null;
        if(jedis.get("category")==null){
            IndexService indexService=new IndexService();
            List<Category> categories=indexService.getCategory();
            Gson gson=new Gson();
            result=gson.toJson(categories);
            jedis.set("category",result);
            System.out.println("查了数据库");
        }else{
            System.out.println("没有查数据库");
            result=jedis.get("category");
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(result);
    }
}
