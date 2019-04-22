package com.shop.web.servlet;

import com.shop.domain.Product;
import com.shop.server.IndexService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "ProInfoServlet")
public class ProInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String pid=request.getParameter("pid");
        IndexService indexService=new IndexService();
        Product product = indexService.getProInfo(pid);
        request.setAttribute("info",product);
        request.getRequestDispatcher("product_info.jsp").forward(request,response);
    }
}
