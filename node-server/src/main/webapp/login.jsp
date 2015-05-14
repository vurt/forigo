<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html id="ng-app" ng-app="app">
<head>
<title>节点管理系统 - 登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<meta name="description" content="overview &amp; stats" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%> 
<base href="<%=basePath%>">
<link rel="stylesheet" href="assets/css/c2-all.css" />

</head>
<body style="background-color: #f5f5f5;" ng-controller="loginController">
	<center>
		<br>
		<div style="height: 60px; width: 400px;"></div>


		<h1 class="text-primary">
			<strong>节点管理系统</strong>
		</h1>

		<div class="panel panel-default text-left" style="width: 400px;">
			<div class="panel-body">
				<form role="form" ng-submit="submitForm()">
					<div class="form-group">
						<label class="text-muted">登录名</label> <input type="text" id="username"
							class="form-control" ng-model="username"  required autofocus>
					</div>
					<div class="form-group">
						<label class="text-muted">密码</label> <input type="password" id="password"
							class="form-control" ng-model="password"  required>
					</div>
					<!-- <div class="form-group">
						<div class="checkbox">
							<label> <input type="checkbox" ng-model="rememberme">
								记住我（公共场所慎用）
							</label>
						</div>
					</div> -->
					<div class="form-group">
						<button type="submit" class="btn btn-primary">登 录</button>
					</div>
				</form>
			</div>
		</div>
	</center>

	<!-- basic scripts -->
	<script src="assets/jquery.min.js"></script>
	<script src="assets/js/c2-all.js"></script>

	<script type="text/javascript">
		app.controller('loginController', [ '$scope', 'SecurityFactory',
				function($scope, SecurityFactory) {
					$scope.submitForm = function() {
						SecurityFactory.login($scope.username, $scope.password);
					};
				} ]);		
	</script>
</body>
</html>
