/*
Transactions Output Class
 */
package bydlt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 *
 * @author kingsley oteng-amoako
 */
public class TransactionOutputIO {
        
        public String id;
	public PublicKey recipient; //also known as the new owner of these coins.
	public float value; //the amount of coins they own
	public String parentTransactionId; //the id of the transaction this output was created in
	
	//Constructor
	public TransactionOutputIO(PublicKey reciepient, float value, String parentTransactionId) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this.recipient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = SignatureUtil.applySha256(SignatureUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == recipient);
	}
}
