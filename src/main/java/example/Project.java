package example;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class Project{
	public static void listFilesForFolder(final File folder) {
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	public static BufferedImage[] splitimage() throws IOException
	{
		File file = new File("/home/nandan/Desktop/2.png"); // I have bear.jpg in my working directory
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis); //reading the image file

        int rows = 16; //You should decide the values for rows and cols variables
        int cols = 16;
        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
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
        //System.out.println("Splitting done");
        
        for (int i = 0; i < imgs.length; i++) {
            ImageIO.write(imgs[i], "jpg", new File("img" + i + ".jpg"));
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
	public static ArrayList<byte[]> servertag() throws NoSuchAlgorithmException, IOException
	{
		File dir = new File("/home/nandan/Desktop/server");
		//System.out.println();
		ArrayList <byte[]> ciphertag= new ArrayList<byte[]>();

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
			 OutputStream fos = new FileOutputStream("/home/nandan/Desktop/server/"+name+" "+flag);
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
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		System.out.println("enter the file name");
		Scanner s=new Scanner(System.in);
		String name=s.nextLine();
		
		
		BufferedImage[] imgs=splitimage();
        System.out.println("Splting done");
		int i=0;
		
		/**ArrayList<byte[]> imageblock=new ArrayList<byte[]>();
		ArrayList<> hashofimage=new ArrayList();
		ArrayList publickey=new ArrayList();
		ArrayList encryptedimage=new ArrayList();
		ArrayList tagofimage=new ArrayList();**/
		
		
		ArrayList<byte[]> imageblock=new ArrayList<byte[]>();
		ArrayList<byte[]> hashofimage=new ArrayList<byte[]>();
		ArrayList<SecretKeySpec> publickey=new ArrayList<SecretKeySpec>();
		ArrayList<byte[]> encryptedimage=new ArrayList<byte[]>();
		ArrayList<byte[]> tagofimage=new ArrayList<byte[]>();
		
		
		for(BufferedImage img:imgs)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
	        byte[] content =baos.toByteArray();
	       // System.out.println("calculating hash of image");
	    
			byte[] k=hash(content);
			//System.out.println("hash of image is");

			//for(byte j:k){
				//System.out.print(j);
			//}
			
			//System.out.println("converting hash value to key");
		    SecretKeySpec secretKey = new SecretKeySpec(k, "AES");
			//System.out.println("key is "+secretKey);
			//System.out.println("encrypting image");
			byte[] encrypted = encryptFile(secretKey, content);
			//System.out.println("encrypted "+encrypted);
			
			//System.out.println("creating tag");
			byte[] tag = hash(encrypted);
			//System.out.println(tag);
			
			imageblock.add(content);
			hashofimage.add(k);
			publickey.add(secretKey);
			encryptedimage.add(encrypted);
			tagofimage.add(tag);
			
		}
		System.out.println("images to blocks");
		for(i=0;i<256;i++)
		{
			
			System.out.print(imageblock.get(i)+"\t");
			
		}
		System.out.println();
		System.out.println("hash of image");
		for(i=0;i<256;i++)
		{
			System.out.print(hashofimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("public key");
		for(i=0;i<256;i++)
		{
			System.out.print(publickey.get(i)+"\t");
		}
		System.out.println();
		System.out.println("cipher text");
		for(i=0;i<256;i++)
		{
			System.out.print(encryptedimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("tag");
		for(i=0;i<256;i++)
		{
			System.out.print(tagofimage.get(i)+"\t");
		}
		System.out.println();
		System.out.println("checking for existance of duplication ");
		System.out.println("getting all the cipher text and converting it to tag");
		ArrayList<byte[]> ciphertag=new ArrayList<byte []>();
		ciphertag=servertag();
		for(i=0;i<ciphertag.size();i++)
		{
			System.out.print(ciphertag.get(i)+"\t");
		}
		System.out.println();
		findduplicate(tagofimage,ciphertag,encryptedimage,name);
		ArrayList<byte[]> decrypted=new ArrayList<byte []>();
		System.out.println("decrypting images");
		decrypted=decryptFile(publickey,encryptedimage);
		saveFile(decrypted,name);
		s.close();
		//final File folder = new File("/home/nandan/Desktop/project");
		//listFilesForFolder(folder);
	}

}
