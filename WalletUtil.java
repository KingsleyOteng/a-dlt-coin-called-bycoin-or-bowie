/*
Wallets Class
 */
package bydlt;
import java.security.*;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class WalletUtil {
    
        public PrivateKey privateKeys;
        public PublicKey publicKeys;
        
        public HashMap<String,TransactionOutputIO> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.
	
        public WalletUtil()  
        {
            try 
                {
                    generateKeyPair();
                } 
                catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e)
                {}
        }
        
    /**
     *
     * @throws RuntimeException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidAlgorithmParameterException
     */
    public final void generateKeyPair() throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            privateKeys = keyPair.getPrivate();
            publicKeys = keyPair.getPublic();
        };
    
    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, TransactionOutputIO> item: Block.UTXOs.entrySet()){
        	TransactionOutputIO UTXO = item.getValue();
            if(UTXO.isMine(publicKeys)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
        }  
		return total;
	}
    
    //Generates and returns a new transaction from this wallet.
	public TransactionsUtil sendFunds(PublicKey receiver,float value ) throws NoSuchAlgorithmException, RuntimeException, NoSuchProviderException, InvalidKeyException, SignatureException {
		if(getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
                
                //create array list of inputs
		ArrayList<TransactionInputIO> inputs = new ArrayList<TransactionInputIO>();
    
		float total = 0;
		for (Map.Entry<String, TransactionOutputIO> item: UTXOs.entrySet()){
			TransactionOutputIO UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInputIO(UTXO.id));
			if(total > value) break;
		}
		
		TransactionsUtil newTransaction = new TransactionsUtil(publicKeys, receiver , value, inputs);
		newTransaction.generateSignature(privateKeys);
		
		for(TransactionInputIO input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
}
