import java.io.*;
import java.util.*;
import java.security.*;
import java.math.*;

public class SimpleCracker {
	
	public static void main(String[] Args) throws IOException {
		ArrayList<String> passWordsList = new ArrayList<String>();
		
		// Extract the passwords from the common-passwords file and store it
		// in passWordsList list variable
		try{
			Scanner file = new Scanner (new File("common-passwords.txt"));
			while (file.hasNext()){
				passWordsList.add(file.next());
			}
			file.close();
		}
		catch(FileNotFoundException exception){
			exception.printStackTrace();
		}
		
		System.out.println("List of password match:");
		System.out.println("#######################");
		
		// Traverse through the shadows file for processing.
		try{
			Scanner shadowFile = new Scanner(new File("shadow-simple.txt"));
			while(shadowFile.hasNext()){
				String shadowFileLine = shadowFile.next();
				
				String[] userDetails = shadowFileLine.split(":");
				String userName = userDetails[0];
				String salt = userDetails[1];
				byte[] userSalt = salt.getBytes();
				String originalPasswordHash = userDetails[2];
				
				for (String commonPassword: passWordsList) {
					String commonPasswordHash = md5PasswordHash(commonPassword, userSalt);
					if(originalPasswordHash.equals(commonPasswordHash)){
						System.out.println(userName+ " : " +commonPassword);
						
					}
				}
			}
			shadowFile.close();
		}
		catch(FileNotFoundException exception){
			exception.printStackTrace();
		}
		
	}
	
	// Calculate the md5 Hash for the password and salt.
	private static String md5PasswordHash(String password, byte[] salt) throws UnsupportedEncodingException{
		MessageDigest messageDigestInstance = null;
		try {
			messageDigestInstance = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigestInstance.reset();
		messageDigestInstance.update(salt);
		String hexPassword;
		hexPassword = toHex(messageDigestInstance.digest(password.getBytes("UTF-8")));
		return hexPassword;
	}
	
	private static String toHex(byte[] bytes)
	{
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length <<1) +"X",bi);
	}
}



