package com.shop.web.servlet;

import com.google.gson.Gson;
import com.shop.domain.*;
import com.shop.server.IndexService;
import com.shop.until.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("all")
//@WebServlet(name = "ProductAllServlet")
public class ProductAllServlet extends BaseServlet {

//清空购物车
public  void clearCart(HttpServletRequest request,HttpServletResponse response) throws  ServletException, IOException{
    HttpSession session=request.getSession();
    session.removeAttribute("cart");
    response.sendRedirect(request.getContextPath()+"/cart.jsp");
}

//删除单个商品
    public  void delPro(HttpServletRequest request,HttpServletResponse response) throws  ServletException, IOException{
        String pid =request.getParameter("pid");
        HttpSession session=request.getSession();
        Cart cart= (Cart) session.getAttribute("cart");
        if (cart != null) {
            Map<String, CartItem> items = cart.getCartItems();
            cart.setTotal(cart.getTotal()-items.get(pid).getSubTotal());
            items.remove(pid);
            cart.setCartItems(items);
        }

        session.setAttribute("cart",cart);
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
//    添加购物车
    public void addShopCar(HttpServletRequest request,HttpServletResponse response) throws  ServletException, IOException{
        String pid=request.getParameter("pid");
        HttpSession session=request.getSession();
        int num= Integer.parseInt(request.getParameter("num"));
        CartItem cartItem=new CartItem();
        IndexService indexService=new IndexService();
        Product product=indexService.getProInfo(pid);
        cartItem.setProduct(product);
        cartItem.setBuyNum(num);
        cartItem.setSubTotal(num*product.getShop_price());
        Cart cart= (Cart) session.getAttribute("cart");
        double newsubtotal=0.0;
        if(cart==null){
            cart=new Cart();
        }
        System.out.print(num);

        Map<String, CartItem> items = cart.getCartItems();
        if(items.containsKey(pid)){
            CartItem cartItem1=items.get(pid);
            int oldSubNum=cartItem1.getBuyNum();
            oldSubNum+=num;
            cartItem1.setBuyNum(oldSubNum);
            double oldSubTotal=cartItem1.getSubTotal();
            oldSubTotal=cartItem1.getSubTotal();
            newsubtotal =num*product.getShop_price();
            cartItem1.setSubTotal(oldSubTotal+newsubtotal);
        }else{
            cart.getCartItems().put(pid,cartItem);
            newsubtotal = num*product.getShop_price();
        };
        double total=cart.getTotal()+newsubtotal;
        cart.setTotal(total);
        session.setAttribute("cart",cart);
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
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
