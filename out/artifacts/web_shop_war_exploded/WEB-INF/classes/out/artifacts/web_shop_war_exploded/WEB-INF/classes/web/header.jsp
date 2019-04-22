<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<li><a href="login.jsp">登录</a></li>
			<li><a href="register.jsp">注册</a></li>
			<li><a href="cart.jsp">购物车</a></li>
			<li><a href="order_list.jsp">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="menu">
					<%--<li class="active"><a href="product_list.htm">手机数码<span class="sr-only">(current)</span></a></li>--%>
					<%--<li><a href="#">电脑办公</a></li>--%>
					<%--<li><a href="#">电脑办公</a></li>--%>
					<%--<li><a href="#">电脑办公</a></li>--%>
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>
<script src="js/jquery-1.11.3.min.js"></script>
<script>
	$(function () {
        var str=''
        $.post('${pageContext.request.contextPath}/getCategory',
			function(data){
            data = JSON.parse(data);
			    console.log(data.length)
				for(var i=0;i<data.length;i++){
			        console.log(i)
			        str+="<li><a href='${pageContext.request.contextPath}/getProByCid?cid='"+data[i].cid+">"+data[i].cname+"</a></li>"
				}
				$('#menu').html(str)
				console.log(str)
			}
		)
    })
</script>