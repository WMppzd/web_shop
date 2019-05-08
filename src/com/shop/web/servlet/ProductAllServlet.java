package com.shop.web.servlet;

import com.google.gson.Gson;
import com.shop.domain.*;
import com.shop.server.IndexService;
import com.shop.until.CommonUtils;
import com.shop.until.JedisPoolUtils;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("all")
//@WebServlet(name = "ProductAllServlet")
public class ProductAllServlet extends BaseServlet {
//付款更新状态
    public  void paymoney(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String[]> pop = request.getParameterMap();
        Order order=new Order();
        try {
            BeanUtils.populate(order, pop);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        IndexService service=new IndexService();
        service.paypaymoney(order);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("<h1>付款成功！等待商城进一步操作！等待收货...</h1>");
    }



// 提交订单
    public  void submitOrder(HttpServletRequest request,HttpServletResponse response) throws  ServletException, IOException{
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if(user==null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }
//        封装一个order
//        private String oid;//该订单的订单号
//        private Date ordertime;//下单时间
//        private double total;//该订单的总金额
//        private int state;//订单支付状态 1代表已付款 0代表未付款
//
//        private String address;//收货地址
//        private String name;//收货人
//        private String telephone;//收货人电话
//
//        private User user;//该订单属于哪个用户
        Cart cart= (Cart) session.getAttribute("cart");


        if(cart!=null){
            Order order=new Order();
            String oid = CommonUtils.getId();
            order.setOid(oid);
            order.setOrdertime(new Date());
            order.setTotal(cart.getTotal());
            order.setState(0);
            order.setAddress(null);
            order.setName(null);
            order.setTelephone(null);
            order.setUser(user);
            Map<String, CartItem> cartItem = cart.getCartItems();

//            private String itemid;//订单项的id
//            private int count;//订单项内商品的购买数量
//            private double subtotal;//订单项小计
//            private Product product;//订单项内部的商品
//            private Order order;//该订单项属于哪个订
            for (Map.Entry<String,CartItem>entry:cartItem.entrySet()
                 ) {
                OrderItems items=new OrderItems();
                items.setItemid(CommonUtils.getId());
                items.setCount(entry.getValue().getBuyNum());
                items.setSubtotal(entry.getValue().getSubTotal());
                items.setProduct(entry.getValue().getProduct());
                items.setOrder(order);
                order.getOrderItems().add(items);
            }
            IndexService service=new IndexService();
            service.submitOrder(order);
            session.setAttribute("order", order);
        }

        //页面跳转
        response.sendRedirect(request.getContextPath()+"/order_info.jsp");

    }

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
