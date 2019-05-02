/*
Transations Input Class
 */
package bydlt;

import java.security.*;
import java.util.ArrayList;
/**
 *
 * @author kingsley oteng-amoako
 */
public class TransactionInputIO {
    
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TransactionOutputIO UTXO; //Contains the Unspent transaction output
	
	public TransactionInputIO(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
    
    
    
};
