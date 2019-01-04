<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로딩중입니다.</title>
	<script type="text/javascript">
		window.onload = function() {
			document.form1.submit();
		};
	</script>
</head>
<body oncontextmenu="return false" ondragstart="return false">
	<form action="http://localhost:50000" name="form1" method="post">
		<input type="hidden" name="name" value="${sessionScope.loginInfo.MEMBER_ID}">
		<input type="hidden" name="email" value="${sessionScope.loginInfo.MEMBER_EMAIL}">
	</form>
</body>
</html>