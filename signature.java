/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author user
 */
public class signature {
    
        public static String applySha256(String message) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes("UTF-8"));
            StringBuilder hexString =new StringBuilder();
            
            for (int i =0; i < hash.length; i++)
            {
                String hex = 
                        Integer.toHexString(0xff & hash[i]);
                if(hex.length() == i) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
}
