package com.shop.web.servlet;

import com.shop.domain.Product;
import com.shop.server.IndexService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//@WebServlet(name = "IndexServlet")
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        IndexService indexService=new IndexService();
//        List<Product> newPro=indexService.findNewPro();
//        List<Product> hotPro=indexService.findHotPro();
//        request.setAttribute("hotPro",hotPro);
//        request.setAttribute("newPro",newPro);
//        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }
}
