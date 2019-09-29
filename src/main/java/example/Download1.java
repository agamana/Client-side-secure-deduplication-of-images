package example;

 
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.mysql.cj.jdbc.Blob;

/**
 * Servlet implementation class Download
 */
public class Download1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Download1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String fname=request.getParameter("fname");
		String  username;
		ArrayList<byte []>file = new ArrayList<byte []>();
		ArrayList<SecretKeySpec>key = new ArrayList<SecretKeySpec>();
		ArrayList<byte []>a = new ArrayList<byte []>();
		int width=0,height=0;
		try{
			 HttpSession session =request.getSession();
             username=(String) session.getAttribute("username");
           
			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		 	 //creating connection with the database 
		         Connection con=DriverManager.getConnection
		                        ("jdbc:mysql://localhost:3306/nandan","root","hello");
		         
		        PreparedStatement ps = con.prepareStatement
                       ("select * from server1 where email=? and fname=?");
		        ps.setString(1, username);
		        ps.setString(2, fname);
		        ResultSet rs =ps.executeQuery();
		       
		         while(rs.next()){
		        	 
		        	 int id=rs.getInt("ID");
		        	 int id1=rs.getInt("Id1");
		        	 if(id1==0){
		        		 
		        	 
		        	 
			        	 Blob blob=(Blob) (rs.getBlob("file"));
			        	 int blobLength = (int) blob.length();  
			        	 byte[] blobAsBytes = blob.getBytes(1, blobLength);
			        	 
			        	 file.add(blobAsBytes);
			        	 //release the blob and free up memory. (since JDBC 4.0)
			        	 blob.free();
	
	
			        	 Blob b=(Blob) (rs.getBlob("ckey"));
			        	 int blobLength1 = (int) b.length();  
			        	 byte[] blobAsBytes1 = b.getBytes(1, blobLength1);
			        	
			        	 a.add(blobAsBytes1);
			        	 b.free();
		        	 
		        	 }
		        	 else{
		        		 
		        		 PreparedStatement ps1=con.prepareStatement(" select file,ckey from server1 where id in(select id1 from server1 where id=?);");
		        		 
		        		 ps1.setInt(1, id);
		        		 
		        		 ResultSet rs2=ps1.executeQuery();
		        		 
		        		 while(rs2.next()){
		        			 
		        			 Blob blob=(Blob) (rs2.getBlob("file"));
				        	 int blobLength = (int) blob.length();  
				        	 byte[] blobAsBytes = blob.getBytes(1, blobLength);
				        	 
				        	 file.add(blobAsBytes);
				        	 //release the blob and free up memory. (since JDBC 4.0)
				        	 blob.free();
		
		
				        	 Blob b=(Blob) (rs2.getBlob("ckey"));
				        	 int blobLength1 = (int) b.length();  
				        	 byte[] blobAsBytes1 = b.getBytes(1, blobLength1);
				        	
				        	 a.add(blobAsBytes1);
				        	 b.free();
		        			 
		        		 }
		        		 
		        		 
		        		 
		        	 }
		        	 
		        	 
		         }
		         con.close();
		         for(byte[] in:a){
		        	 SecretKeySpec key2 = new SecretKeySpec(in, 0, in.length, "AES");
		        	 
		        	 key.add(key2);
		         }
		         
		         
		         
		}
		
		
		
		catch(Exception e)
		      {
		          e.printStackTrace();
		      }
	
		
		ArrayList<byte []>decrypt = new ArrayList<byte []>();
		decrypt=decryptFile(key,file);
		
		saveFile(decrypt,"name");
		
		try {
			write(fname,decrypt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 response.setContentType("image/jpg");  
		    ServletOutputStream out;  
		    out = response.getOutputStream();  
		    FileInputStream fin = new FileInputStream("result.jpg");  
		      
		    BufferedInputStream bin = new BufferedInputStream(fin);  
		    BufferedOutputStream bout = new BufferedOutputStream(out);   
		    int ch =0; ;  
		    while((ch=bin.read())!=-1)  
		    {  
		    bout.write(ch);  
		    }  
		      
		    bin.close();  
		    fin.close();  
		    bout.close();  
		    out.close();  
		    
		
		    
		   
		    
		key.clear();
		file.clear();
		a.clear();
		decrypt.clear();
		
		
	}
	
	public static ArrayList<byte[]> decryptFile(ArrayList<SecretKeySpec> secretKey,ArrayList< byte[]> textCryp) {
        Cipher cipher;
        ArrayList<byte[]>decrypted =new ArrayList<byte[]>();
        for(int i=0;i<textCryp.size();i++)
        {
	        try {
	            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	
	            cipher.init(Cipher.DECRYPT_MODE, secretKey.get(i));
	            decrypted.add(cipher.doFinal(Base64.decodeBase64(textCryp.get(i))));
	
	        } catch (Exception e) {
	
	            System.out.println("Error while decrypting: " + e.toString());
	        }
	        
        }
        return decrypted;
    }
	
public static void saveFile(ArrayList<byte[]> bytes,String name) throws IOException {

        
        
        for( int i=0;i<bytes.size();i++)
		{
        	FileOutputStream fos = new FileOutputStream("/home/nandan/Desktop/images/"+name+""+i+".jpg");
            fos.write(bytes.get(i));
            fos.close();
		}

    }

void write(String fname,ArrayList<byte []> decrypted) throws IOException, SQLException{
	
	int width = 0,height = 0;
	
	
	Connection con1=DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/nandan","root","hello");
 
 PreparedStatement ps = con1.prepareStatement("select * from imagesize where name=?;");
 ps.setString(1,fname);
 
 ResultSet r = ps.executeQuery();
 
 while(r.next()){
	  width=r.getInt("width");
	 height=r.getInt("height");
 }
 
 System.out.println("value is "+width+" "+height);
con1.close();
	
	
	
	
	
	
	
	 int rows = 4; //You should decide the values for rows and cols variables
     int cols = 4;
      int chunks = rows * cols;

      int chunkWidth = width / cols; // determines the chunk width and height
      int chunkHeight = height / rows;
     
     	BufferedImage imgs[] = new BufferedImage[chunks];
     	 for(int i=0;i<decrypted.size();i++){
     		ByteArrayInputStream bais = new ByteArrayInputStream(decrypted.get(i));
     	    try {
     	        
  			imgs[i]=(ImageIO.read(bais));
     	    } catch (IOException e) {
     	        throw new RuntimeException(e);
     	    }
     	}
      BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
      int num = 0;  
      for (int i = 0; i < rows; i++) {  
          for (int j = 0; j < cols; j++) {  
              result.createGraphics().drawImage(imgs[num], chunkWidth * j, chunkHeight * i, null);  
              num++;  
          }  
      }  
      
      System.out.println("Image concatenated.....");  
      ImageIO.write(result, "jpeg", new File("result.jpg"));  

      
      
	
	 
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		System.out.println("hello");
	}

}
