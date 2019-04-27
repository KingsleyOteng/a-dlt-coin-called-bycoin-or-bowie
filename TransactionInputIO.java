/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;

import java.security.*;
import java.util.ArrayList;
/**
 *
 * @author user
 */
public class TransactionInputIO {
    
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TransactionOutputIO UTXO; //Contains the Unspent transaction output
	
	public TransactionInputIO(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
    
    
    
};
