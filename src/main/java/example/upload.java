package example;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Base64;

import com.mysql.cj.jdbc.Blob;


@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)

/**
 * Servlet implementation class upload
 */
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	int flag=0;
	 static BufferedImage image;
	 static int chunkWidth;
	 static int  chunkHeight;
	 static int chunks;
	 static String username;
	static ArrayList<SecretKeySpec> publickey=new ArrayList<SecretKeySpec>();

		static ArrayList<byte[]> decrypted=new ArrayList<byte []>();
		static ArrayList<byte[]> encryptedimage=new ArrayList<byte[]>();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session =request.getSession();
        
		if(session.getAttribute("username")==null){
			response.sendRedirect("index.jsp");
		}
		final long startime=System.nanoTime();
		username=(String) session.getAttribute("username");
		//InputStream inputStream = null; // input stream of the upload file
        
        // obtains the upload file part in this multipart request
		Part filePart = request.getPart("img");
        InputStream fileContent = filePart.getInputStream();
         
       // System.out.println("enter the file name");
		
        String name=request.getParameter("name");
        System.out.println(name);
		
		BufferedImage[] imgs=splitimage(fileContent);
        System.out.println("Splting done");
		int i=0;
		
		/**ArrayList<byte[]> imageblock=new ArrayList<byte[]>();
		ArrayList<> hashofimage=new ArrayList();
		ArrayList publickey=new ArrayList();
		ArrayList encryptedimage=new ArrayList();
		ArrayList tagofimage=new ArrayList();**/
		
		
		ArrayList<byte[]> imageblock=new ArrayList<byte[]>();
		ArrayList<byte[]> hashofimage=new ArrayList<byte[]>();
		//ArrayList<SecretKeySpec> publickey=new ArrayList<SecretKeySpec>();
		//ArrayList<byte[]> encryptedimage=new ArrayList<byte[]>();
		ArrayList<byte[]> tagofimage=new ArrayList<byte[]>();
		
		
		for(BufferedImage img:imgs)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
	        byte[] content =baos.toByteArray();
	       // System.out.println("calculating hash of image");
	    
			byte[] k=null;
			try {
				k = hash(content);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("hash of image is");

			//for(byte j:k){
				//System.out.print(j);
			//}
			
			//System.out.println("converting hash value to key");
		    SecretKeySpec secretKey = new SecretKeySpec(k, "AES");
			System.out.println("key is "+secretKey);
			
			
			
			
			//System.out.println("encrypting image");
			byte[] encrypted = encryptFile(secretKey, content);
			//System.out.println("encrypted "+encrypted);
			
			//System.out.println("creating tag");
			byte[] tag=null;
			try {
				tag = hash(encrypted);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(tag);
			
			imageblock.add(content);
			hashofimage.add(k);
			publickey.add(secretKey);
			encryptedimage.add(encrypted);
			tagofimage.add(tag);
			
		}
		System.out.println("images to blocks");
		for(i=0;i<imageblock.size();i++)
		{
			
			System.out.print(imageblock.get(i)+"\t");
			
		}
		System.out.println();
		System.out.println("hash of image");
		for(i=0;i<hashofimage.size();i++)
		{
			System.out.print(hashofimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("public key");
		for(i=0;i<publickey.size();i++)
		{
			System.out.print(publickey.get(i)+"\t");
		}
		System.out.println();
		System.out.println("cipher text");
		for(i=0;i<encryptedimage.size();i++)
		{
			System.out.print(encryptedimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("tag");
		for(i=0;i<tagofimage.size();i++)
		{
			System.out.print(tagofimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("checking for existance of duplication ");
		System.out.println("getting all the cipher text and converting it to tag");
		ArrayList<byte[]> ciphertag=new ArrayList<byte []>();
		
		
		
		System.out.println("\nSize is "+ciphertag.size());
		
		HashMap<byte[],Integer> map=servertag1();
		
		
		
		try {
			findduplicate1(map,tagofimage,name);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		
		
		
		
		
		
		System.out.println();
		
		//System.out.println("decrypting images");
		//decrypted=decryptFile(publickey,encryptedimage);
		//saveFile(decrypted,name);
		
		//final File folder = new File("/home/nandan/Desktop/project");
		//listFilesForFolder(folder);
		
		
		
		
		
		// get base64 encoded version of the key
		
		
		 try{
			 
			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		 	 //creating connection with the database 
		         Connection con=DriverManager.getConnection
		                        ("jdbc:mysql://localhost:3306/nandan","root","hello");
		         PreparedStatement ps; 
		         
		         
		         ps=con.prepareStatement("insert into imagesize values(?,?,?);");
		         ps.setString(1,name);
		         ps.setInt(2,image.getWidth() );
		         ps.setInt(3,image.getHeight());
		         ps.executeUpdate();
		         flag=1;
		        con.close();
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		      }
		
			
		chunkWidth=0;
		chunkHeight=0;
		image=null;
		chunks=0;
		username=null;
		decrypted.clear();
		publickey.clear();
		encryptedimage.clear();
		final long duration=System.nanoTime()-startime;
		double seconds =(double)duration/1000000000.0;
		System.out.println("Time is "+seconds);
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        if(flag==1)
        out.println("<script type=\"text/javascript\">alert(\"Inserted Successfully\");</script>");
		
		response.sendRedirect("Home.jsp");
	}
public static void listFilesForFolder(final File folder) {
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	public static BufferedImage[] splitimage(InputStream inputStream) throws IOException
	{
		
         image = ImageIO.read(inputStream); //reading the image file

        int rows = 4; //You should decide the values for rows and cols variables
        int cols = 4;
         chunks = rows * cols;

          chunkWidth = image.getWidth() / cols; // determines the chunk width and height
         chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }
        System.out.println("Splitting done");
        
        for (int i = 0; i < imgs.length; i++) {
            ImageIO.write(imgs[i], "jpg", new File("/home/nandan/workspace/Servletjspdemo/blocks/"+"name" + i + ".jpg"));
        }
        return imgs;
	}
	public static byte[] hash(byte[] data1)throws IOException, NoSuchAlgorithmException
	{
		byte[] hash1=null;
		//System.out.println("Start");
		MessageDigest md = MessageDigest.getInstance("SHA-256");

        //System.out.println("Md object is"+md.toString());
        md.update(data1);
        
        hash1 = md.digest(data1);
 		
 		md.reset();
 		

        return hash1;
        
        
	}
	public static byte[] encryptFile(SecretKey secretKey, byte[] content) 
	{
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            encrypted = Base64.encodeBase64(cipher.doFinal(content));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }
        return encrypted;

    }
	
	public static HashMap<byte[],Integer> servertag1(){
		
		ArrayList <byte[]> ciphertag= new ArrayList<byte[]>();
		
		HashMap<byte[],Integer> map=new HashMap<byte[],Integer>();
		

		try{
			
          
			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		 	 //creating connection with the database 
		         Connection con=DriverManager.getConnection
		                        ("jdbc:mysql://localhost:3306/nandan","root","hello");
		         
		        PreparedStatement ps = con.prepareStatement
                      ("select * from server1 where email=? and Id1 is null");
		        ps.setString(1, username);
		        //ps.setString(2, fname);
		        ResultSet rs =ps.executeQuery();
		       
		         while(rs.next()){
		        	 Blob blob=(Blob) (rs.getBlob("file"));
		        	 int blobLength = (int) blob.length();  
		        	 byte[] blobAsBytes = blob.getBytes(1, blobLength);
		        	 
		        	 byte servertag[]=hash(blobAsBytes);
		        	 
		        	int num=rs.getInt("ID");
		        	 //release the blob and free up memory. (since JDBC 4.0)
		        	
		        	map.put(servertag, num);
		        	
		        	 blob.free();


		        	
		         }
		         
		         System.out.println("map "+map.size());
		}
		         catch(Exception e){
		        	 
		         }

		
		
		
		return map;
		
	}
	
	
	public static ArrayList<byte[]> servertag() throws NoSuchAlgorithmException, IOException
	{
		ArrayList <byte[]> ciphertag= new ArrayList<byte[]>();
		
		
		
		
		File dir = new File("/home/nandan/workspace/Servletjspdemo/server/");
		//System.out.println();
		

		for (File file : dir.listFiles()) 
		{ 
			byte[] cipher = new byte[(int) file.length()];
	        try 
	       {
	            FileInputStream fileInputStream = new FileInputStream(file);
	            fileInputStream.read(cipher);
	           
	            	//System.out.print(cipher+"\t");
	           
	            fileInputStream.close();
	       }
	       catch (FileNotFoundException e)
	       {
	                   System.out.println("File Not Found.");
	                   e.printStackTrace();
	       }
	       catch (IOException e1)
	       {
	                System.out.println("Error Reading The File.");
	                 e1.printStackTrace();
	       }
	       
	       byte servertag[]=hash(cipher);
	       ciphertag.add(servertag);
		}
		return ciphertag;
	}
	
	
	public static void findduplicate1(HashMap<byte[],Integer> map1,ArrayList<byte[]> tag1,String name) throws SQLException, ClassNotFoundException{
		
		
		int flag=0;
		 Class.forName("com.mysql.jdbc.Driver");

	 	 //creating connection with the database 
	         Connection con=DriverManager.getConnection
	                        ("jdbc:mysql://localhost:3306/nandan","root","hello");
	         PreparedStatement ps =con.prepareStatement
	                             ("insert into server1(email,fname,file,ckey) values(?,?,?,?);");
	         
	         PreparedStatement ps1=con.prepareStatement("insert into server1(email,fname,Id1) values(?,?,?);");
	         
	         
	         
	         
		
		if(map1.size()==0){
			
			for(int i=0;i<tag1.size();i++ ){
				InputStream a=new ByteArrayInputStream(encryptedimage.get(i));
				byte [] c =publickey.get(i).getEncoded();
				InputStream a1=new ByteArrayInputStream(c);
				ps.setString(1,username);
				ps.setString(2,name);
				ps.setBinaryStream(3,a);
				ps.setBinaryStream(4,a1);
				ps.executeUpdate();
				
				
			}
			con.close();
			return;
			
		}
		
		else{
			for(int i=0;i<tag1.size();i++){
				for(Map.Entry<byte[],Integer> entry :map1.entrySet()){
					 
					byte [] ciphert=entry.getKey();
					int id=entry.getValue();
					if(Arrays.equals(tag1.get(i), ciphert)){
						
						//insert into server1(email,fname,ID1) values(username,name,id)
						flag=1;
						ps1.setString(1, username);
						ps1.setString(2, name);
						ps1.setInt(3, id);
						
						ps1.executeUpdate();
						break;
						
					}
					//insert into server1(email,fname,file,ckey) values(?,?,?,?);
					
					
					
				}
				if(flag==0){
					InputStream a=new ByteArrayInputStream(encryptedimage.get(i));
					byte [] c =publickey.get(i).getEncoded();
					InputStream a1=new ByteArrayInputStream(c);
					ps.setString(1,username);
					ps.setString(2,name);
					ps.setBinaryStream(3,a);
					ps.setBinaryStream(4,a1);
					ps.executeUpdate();
					
				}
				flag=0;
			}
			
			
		}
		 
    
	
	
		con.close();
	
	
	
	}
	
	
	
	public static void findduplicate(ArrayList<byte[]> tag,ArrayList<byte[]> ciphertag,ArrayList<byte[]> encryptedimage,String name) throws IOException, NoSuchAlgorithmException
	{
		
			int flag=0;
			ArrayList<byte[]> newcipher= new ArrayList<byte[]>();

			if(ciphertag.size()==0)
			{
				for(int i=0;i<tag.size();i++)
				{
					newcipher.add(encryptedimage.get(i));
				}
			}
			else
			{
				for(int i=0;i<tag.size();i++)
				{
					for(int j=0;j<ciphertag.size();j++)
					{
						if(java.util.Arrays.equals(tag.get(i),ciphertag.get(j)))
								{
									flag=1;
									break;
								}
						
					}
					if(flag==0)
						newcipher.add(encryptedimage.get(i));
					flag=0;
				}
			}
			System.out.println("ciphers which were not in the server");
		for(int i=0;i<newcipher.size();i++)
		{
			System.out.print(newcipher.get(i)+"\t");
		}
		System.out.println();
		for(int i=0;i<newcipher.size();i++)
		{
			 OutputStream fos = new FileOutputStream("/home/nandan/workspace/Servletjspdemo/server/"+name+" "+flag);
			 fos.write(newcipher.get(i));
		     fos.close();
		     flag++;
		}
		 
	       
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
	




	    
	
	
   
    public upload() {
        super();
        
    }

}
