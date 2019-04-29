/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;

import java.util.Base64;

/**
 *
 * @author user
 */
public class SignatureUtil {
    
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
        
    public static String getStringFromKey(Key key)
        {
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }
        
    /**
     *
     * @param privateKey
     * @param input
     * @return
     * @throws RuntimeException
     */
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
        {
            Signature dsa;
            byte[] output;
            output = new byte[0];
            
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
            
            return output;
        }
    
    public static boolean verifyECDSASig(PublicKey publicKey, String message, byte[] signet) throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
        {
            Signature ecdsaVerify;
            ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(message.getBytes());
            return ecdsaVerify.verify(signet);
        }
    
    //Tacks in array of transactions and returns a merkle root.
    public static String getMerkleRoot(ArrayList<TransactionsUtil> transactions) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException {
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(TransactionsUtil transaction : transactions) {
			previousTreeLayer.add(transaction.transactionId);
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
}
