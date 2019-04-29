package com.shop.web.servlet;

import com.google.gson.Gson;
import com.shop.domain.BeanProByCid;
import com.shop.domain.Category;
import com.shop.domain.Product;
import com.shop.server.IndexService;
import com.shop.until.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
@SuppressWarnings("all")
//@WebServlet(name = "ProductAllServlet")
public class ProductAllServlet extends BaseServlet {
//获取商品详细信息
    public void getProInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
                    List list= Arrays.asList(pidArr);
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

        pid = request.getParameter("pid");
        indexService = new IndexService();
        product = indexService.getProInfo(pid);
        request.setAttribute("info",product);

        request.getRequestDispatcher("product_info.jsp").forward(request,response);
    }
//    获取首页资源
public void getIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexService indexService=new IndexService();
        List<Product> newPro=indexService.findNewPro();
        List<Product> hotPro=indexService.findHotPro();
        request.setAttribute("hotPro",hotPro);
        request.setAttribute("newPro",newPro);
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }


//    获取头分类资源
public void getCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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


//    通过cid获取列表资源
    public void ProByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        String currentPagestr= request.getParameter("currentPage");
        if (currentPagestr==null) currentPagestr="1";
        int currentPage= Integer.parseInt(currentPagestr);
//        int currentCount= Integer.parseInt(request.getParameter("currentCount"));
        int currentCount = 12;
        IndexService indexService=new IndexService();
        BeanProByCid<Product> proByCid = indexService.getProBycid(cid,currentPage,currentCount);
//        Gson gson=new Gson();
//        String res=gson.toJson(proByCid);
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
        request.setAttribute("pro",proByCid);
        request.setAttribute("cid",cid);
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }

}
