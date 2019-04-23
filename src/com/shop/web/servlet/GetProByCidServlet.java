package com.shop.web.servlet;

import com.google.gson.Gson;
import com.shop.domain.BeanProByCid;
import com.shop.domain.Product;
import com.shop.server.IndexService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
<<<<<<< HEAD
import javax.servlet.http.Cookie;
=======
>>>>>>> 819f2358c27cb5e171a648aa5b7cc6e1918d693a
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
<<<<<<< HEAD
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Arrays;
=======
>>>>>>> 819f2358c27cb5e171a648aa5b7cc6e1918d693a
import java.util.List;

//@WebServlet(name = "GetProByCidServlet")
public class GetProByCidServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        String currentPagestr= request.getParameter("currentPage");
        if (currentPagestr==null) currentPagestr="1";
        int currentPage= Integer.parseInt(currentPagestr);
//        int currentCount= Integer.parseInt(request.getParameter("currentCount"));
        int currentCount = 12;
        IndexService indexService=new IndexService();
        BeanProByCid<Product>  proByCid = indexService.getProBycid(cid,currentPage,currentCount);
//        Gson gson=new Gson();
//        String res=gson.toJson(proByCid);
<<<<<<< HEAD
        List<Product> ProList=new ArrayList<Product>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie:cookies
                 ) {
                if("pids".equals(cookie.getName())){
                    String pids=cookie.getValue();
                    String[] Arrays=pids.split("-");
                    for (String pid:Arrays
                         ) {
                        Product product = indexService.getProInfo(pid);
                        ProList.add(product);
                    }
                }
            }
        }
        request.setAttribute("his",ProList);
=======
>>>>>>> 819f2358c27cb5e171a648aa5b7cc6e1918d693a
        request.setAttribute("pro",proByCid);
        request.setAttribute("cid",cid);
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }
}
