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


input[type=email], input[type=password], input[type=text]{
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
}


</style>


		</head>
<body bgcolor="">
<center>

		<form method="post" action="register">


			<h4>REGISTER FORM</h4>
                
                
				<div class="container">
		        
		        
		        <label><b>NAME</b></label><br>
		        <i class="fa fa-user"></i>
                <input  type="text" name="name"  placeholder=" Enter Name" id="ur_mail" required > <br> 
				
				
				<label><b>E-MAIL</b></label><br>
			<i class="fa fa-user"></i>
			<input type="email"  placeholder=" Enter E-mail " name="email" id="ur_mail" required>
			<br>
			
	
			<label><b>PASSWORD</b></label><br>
			<i class="fa fa-lock"></i>
			<input type="password" placeholder="Enter Password " name="pass" id="ur_pass" required></br>
	
				</div>
                
				
		 <button id="r_sub" type="submit">Register</button>
 

			
			</form>
			<h4>Already user?<a href="index.jsp"><h3> Login here</h3> </a>
		
		</body>
		
		</center>


</html>