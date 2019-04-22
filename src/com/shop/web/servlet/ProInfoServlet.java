package com.shop.web.servlet;

import com.shop.domain.Product;
import com.shop.server.IndexService;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import jdk.nashorn.internal.runtime.ListAdapter;
import sun.security.ssl.SunJSSE;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//@WebServlet(name = "ProInfoServlet")
public class ProInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid=request.getParameter("pid");
        String cid=request.getParameter("cid");
        String currentPage=request.getParameter("currentPage");
        IndexService indexService=new IndexService();
        Product product = indexService.getProInfo(pid);

        String pids=pid;
        Cookie[] Cookies = request.getCookies();
        System.out.println(Cookies);
        if(Cookies!=null){
            for (Cookie cookie:Cookies
                    ) {
                if("pids".equals(cookie.getName())){
                    pids=cookie.getValue();
                    String[] pidArr=pids.split("-");
                     List list=Arrays.asList(pidArr);
//                    LinkedList pidsList= (LinkedList) list;
                    LinkedList<String> pidsList = new LinkedList<String>(list);
                    if(pidsList.contains(pid)){
                        pidsList.remove(pid);
                        pidsList.addFirst(pid);
                    }else{
                        pidsList.addFirst(pid);
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i=0;i<pidsList.size()&&i<7;i++){
                        sb.append(pidsList.get(i));
                        sb.append("-");
                    }
                    System.out.println(sb.toString());
                    pids=sb.substring(0,sb.length()-1);
                }
            }
        }
        Cookie cookie=new Cookie("pids",pids);
        response.addCookie(cookie);
        request.setAttribute("info",product);
        request.setAttribute("cid",cid);
        request.setAttribute("currentPage",currentPage);
        request.getRequestDispatcher("product_info.jsp").forward(request,response);
    }
}
