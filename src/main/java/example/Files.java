package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Files
 */
public class Files extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Files() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
HttpSession session =request.getSession();
        
		if(session.getAttribute("username")==null){
			response.sendRedirect("index.jsp");
		}
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html><head>");
		try{
			
           String  username=(String) session.getAttribute("username");
           
			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		 	 //creating connection with the database 
		         Connection con=DriverManager.getConnection
		                        ("jdbc:mysql://localhost:3306/nandan","root","hello");
		         
		        PreparedStatement ps = con.prepareStatement
                       ("Select distinct fname from server1 where email=?");
		        ps.setString(1, username);
		        ResultSet rs =ps.executeQuery();
		        out.println("<h3>Images which were uploaded</h3>");
		        out.println("");
		         while(rs.next()){
			         String s=rs.getString("fname");
			         out.println("<h6>"+s+"</h6>");
			         out.println("");
		         }
		         out.println("<style>form {border: 3px solid #f1f1f1;border-radius: 5px;width : 30%;input[type=email], input[type=password], input[type=text]{width: 75%;padding: 12px 20px;margin: 8px 0;display: inline-block;border: 1px solid #ccc;border-radius: 5px;box-sizing: border-box;background-color:FFFFCC;}button {background-color: #66CC33; color: white;padding: 14px 20px;margin: 8px 0;border: none;border-radius: 5px;cursor: pointer;width: 25%;}</style></head><body>");
		         out.println("<center><form action=\"Download1\" method=\"get\"><h3>Enter the image name:</h3><input type = text name=\"fname\"></input><input type=\"submit\" value=\"get image\"></form></center>");
		         out.println("</html></body>");
		         
		        con.close();
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		      }
		
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
