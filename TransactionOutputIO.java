/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 *
 * @author user
 */
public class TransactionOutputIO {
        
        public String id;
	public PublicKey reciepient; //also known as the new owner of these coins.
	public float value; //the amount of coins they own
	public String parentTransactionId; //the id of the transaction this output was created in
	
	//Constructor
	public TransactionOutputIO(PublicKey reciepient, float value, String parentTransactionId) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = SignatureUtil.applySha256(SignatureUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}
}
