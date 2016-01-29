import java.security.*;
import java.io.*;

public class ComputeSHA{
	
	public static void main(String[] args) throws IOException { 
		
		String fileName = "";
		FileInputStream in = null;
		int numBytes = 0;
		byte[] bs = null;
		String hexHash = "";
		StringBuilder sb = new StringBuilder();
		
		// Get the file name that we need to compute the SHA-1 of
		if(args.length == 1){
			fileName = args[0];
		}
		else{
			System.out.println("Error: Needs one argument");
		}
		
		// Read file in
		try{
			in = new FileInputStream(fileName);
			numBytes = in.available();
			bs = new byte[numBytes];
			in.read(bs);
			in.close();		
		}catch(Exception ex){
			// if any error occurs
			ex.printStackTrace();
		}
		
		// Get SHA-1
		try{
            MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] byteHash = md.digest(bs);
            // Convert to Hex
		    for(byte b : byteHash)
			    sb.append(String.format("%02x", b));
		    hexHash = sb.toString();
		}catch(Exception ex){
			// if any error occurs
			ex.printStackTrace();
		}
	
		// Output
		System.out.println(hexHash);
	}
	
}