/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class TransactionsUtil {
    
    public String transactionId;
    public PublicKey senderAddress;
    public PublicKey recipientAddress;
    public float value;
    public byte[] signet;
    
    public ArrayList<TransactionInputIO> ledgerOfInputs = new ArrayList<>();
    public ArrayList<TransactionOutputIO> ledgerOfOutputs = new ArrayList<>();
    
    public static int sequence = 0; 
    
    
    //input method
    public float getInputsValue() {
		float total = 0;
		for(TransactionInputIO i : ledgerOfInputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			total += i.UTXO.value;
		}
		return total;
	}
    
    //outputs method
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutputIO o : ledgerOfOutputs) {
			total += o.value;
		}
		return total;
	}
        
    // initializer
    public TransactionsUtil(PublicKey from_address, PublicKey to_address, float value, ArrayList<TransactionInputIO> ledger)
        {
            this.senderAddress = from_address;
            this.recipientAddress = to_address;
            this.value = value;
            this.ledgerOfInputs = ledger;
        };
    
    private String buildNewHash() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException {
        sequence++;
        return SignatureUtil.applySha256(
                SignatureUtil.getStringFromKey(senderAddress) + 
                SignatureUtil.getStringFromKey(recipientAddress) + 
                Float.toString(value) +
                sequence); 
        };
    
    void generateSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, RuntimeException, NoSuchProviderException, InvalidKeyException, SignatureException
        {
                String message = SignatureUtil.getStringFromKey(senderAddress) + SignatureUtil.getStringFromKey(recipientAddress) + Float.toString(value);
                signet = SignatureUtil.applyECDSASig(privateKey, message);
        }
    
    boolean verifySignature() throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
        {
                String message = SignatureUtil.getStringFromKey(senderAddress) + SignatureUtil.getStringFromKey(recipientAddress) + Float.toString(value);
                return SignatureUtil.verifyECDSASig(senderAddress, message, signet);
        };
    
    public boolean processTransaction() throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, UnsupportedEncodingException
        {
                if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//collect all unpsent transaction inputs
		for(TransactionInputIO i : ledgerOfInputs) {
			i.UTXO = Block.UTXOs.get(i.transactionOutputId);
		}

		//ensure that transaction value falls within the bounds
		if(getInputsValue() < Block.minimumTransaction) {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		
		//generate a transaction output
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = buildNewHash();
		ledgerOfOutputs.add(new TransactionOutputIO( this.recipientAddress, value,transactionId)); //send value to recipient
		ledgerOfOutputs.add(new TransactionOutputIO( this.senderAddress, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//now add outputs to the unspent ledger
		for(TransactionOutputIO o : ledgerOfOutputs) {
			Block.UTXOs.put(o.id , o);
		}
		
		//all transaction inputs may now transation to the unspent bucket; i.e. they may be purged
		for(TransactionInputIO i : ledgerOfInputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			Block.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
        }
    
}
