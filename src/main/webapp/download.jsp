<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
form {
    border: 3px solid #f1f1f1;
	 border-radius: 5px;
	width : 30%;	
	
}

.imgcontainer {
    text-align: center;
    margin: 12px 0 6px 0;
}

img.avatar {
    width: 25%;
    border-radius: 50%;
}

.container {
    padding: 16px;
}

span.psw {
    float: right;
    padding-top: 16px;
}


input[type=file], input[type=password], input[type=text]{
    width: 75%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
	 border-radius: 5px;
    box-sizing: border-box;
	background-color:FFFFCC;
	
	
	
}




button {
    background-color: #66CC33;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
	 border-radius: 5px;
    cursor: pointer;
    width: 25%;
}

button:hover {
    opacity: 0.8;
}

.cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
     margin: left;
    
}


</style>
</head>
<body bgcolor="">

	<%
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
			
		if(session.getAttribute("username")==null){
			response.sendRedirect("index.jsp");
		}
	%>
	
	<center><h1 style="color:purple;">Secure Image Deduplication using DICE protocol</h1></center>
	
	
	<form action="logout" style="float: right;">
	<center>
			<button class="cancelbtn" type="submit">Logout</button>
	</center>
	</form>
	<center>
	<form action="Download1" method="get">
	
		<h4>Download Here</h4>
		
		 
		
		<div class="container">		
		<label><b>Enter Image Name</b></label><br>
		<input type="text" name="fname" required></input>
		
		
		<button id="button" type="submit">Get Image</button>
		
	</form>
	</center>
	<form action="Files" method="get">
		<button id="button1" type="submit">Get Image Names</button>
	</form>
	</div>
	
	
	
		
	
</body>
</html>