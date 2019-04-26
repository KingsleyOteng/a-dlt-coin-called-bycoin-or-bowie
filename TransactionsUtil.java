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
    
    private boolean processTransaction() throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
        {
                return false;
        }
    
}
