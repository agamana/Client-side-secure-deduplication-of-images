package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
		 PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");	
        
        try{
        	
        	Class.forName("com.mysql.jdbc.Driver");

        	 //creating connection with the database 
                Connection con=DriverManager.getConnection
                               ("jdbc:mysql://localhost:3306/nandan","root","hello");
                PreparedStatement ps =con.prepareStatement
                                    ("insert into user values(?,?,?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, pass);
                ps.executeUpdate();
        }catch (Exception e){
        	System.out.println(e);
        	
        }
        
         
        
        
		response.sendRedirect("index.jsp");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
