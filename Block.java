/*
 * AN IMPLEMENTATION OF A CRYPTO COIN FRAMEWORK
 * CONSISTENT WITH NAMAMOTO'S SHA256 PROOF OF WORK 
 */
package bydlt;

/**
 *
 * @author user
 */
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.GsonBuilder;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.HashMap;


public class Block {

    // variables
    public  String newHashSignet;
    public  String priorHashSignet;
    public String merkleRoot;
    public ArrayList<TransactionsUtil> transactions = new ArrayList<>();
    private final String message;
    private final long timeStamp;
    private int nonce;
    
    /**
     *
     */
    
    
    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutputIO> UTXOs = new HashMap<String,TransactionOutputIO>(); //list of all unspent transactions. 
    public static int miningDepth = 0x4;
    public static WalletUtil WalletA;
    public static WalletUtil WalletB;

    /**
     *
     */
    public static int minimumTransaction = 0x5;
    
    /**
     *
     * @param data
     * @param previousHash
     * @throws UnsupportedEncodingException
     * @throws RuntimeException
     * @throws NoSuchAlgorithmException
     */
    public Block(String data, String previousHash) throws UnsupportedEncodingException , RuntimeException , NoSuchAlgorithmException
        {
                // setters
                this.message = data;
                this.priorHashSignet = previousHash;
                this.timeStamp = new Date().getTime();
                this.newHashSignet = calculatedhash();
                
        };
    
    /**
     *
     * @return 
     * @throws UnsupportedEncodingException
     * @throws RuntimeException
     * @throws NoSuchAlgorithmException
     */
    public final String calculatedhash() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException 
        {
            // hashing script
            String calculatedhash = SignatureUtil.applySha256
            (
                priorHashSignet + 
                Long.toString(timeStamp) + 
                Integer.toString(nonce) + 
                message
            );
        
            return calculatedhash;
        };
    
    /**
     * @return 
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static Boolean isHashCodeCheckValid() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException
        {
            Block currentBlock;
            Block previousBlock;
            
            // generate a new hash block to be mined
            // String hashTarget = new String(new char[miningDepth]).replace('\0','0');
            
            for(int i=1; i< blockChain.size(); i++)
                {
                    currentBlock = blockChain.get(i);
                    previousBlock = blockChain.get(i-1);
                    
                    //reclaculate and compare with the registered hash
                    if(!currentBlock.newHashSignet.equals(currentBlock.calculatedhash()))
                    {
                        System.out.println("Hash check found an error in current block");
                        return false;
                    }
                    
                    //compare previous hash and registered hash
                    if(!previousBlock.newHashSignet.equals(currentBlock.priorHashSignet))
                    {
                        System.out.println("Hash check found an error in previous block");
                        return false;
                    }
                }
            
            return true;
        };
    
    /**
     * @param miningDepth
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    public void miner(int miningDepth) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException
        {
            String mine = new String(new char[miningDepth]).replace('\0', '0');
            while (!newHashSignet.substring(0, miningDepth).equals(mine))
                {
                        nonce ++;
                        newHashSignet = calculatedhash();
                }
            
            System.out.println("New Block [" + newHashSignet + "] Succesfully Mined !!! Isn't that awesome.");
        };
    
        /**
     * @param transaction
     * @return 
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.NoSuchProviderException
     * @throws java.security.InvalidKeyException
     * @throws java.security.SignatureException
     */
        //Add transactions to this block
	public boolean addTransaction(TransactionsUtil transaction) throws RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((!"0".equals(priorHashSignet))) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
        };
    
    /**
     * @param args the command line arguments
     * reference medium.com discussions
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.NoSuchProviderException
     * @throws java.security.InvalidKeyException
     * @throws java.security.SignatureException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, RuntimeException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException 
        {
        
        blockChain.add(new Block("They call me block", "0"));
        System.out.println("Trying to 'mine' block 1... ");
        blockChain.get(0).miner(miningDepth);
        
        blockChain.add(new Block("They call me block", blockChain.get(blockChain.size()-1).newHashSignet));
        System.out.println("Trying to 'mine' block 2... ");
        blockChain.get(1).miner(miningDepth);
        
        blockChain.add(new Block("They call me block", blockChain.get(blockChain.size()-1).newHashSignet));
        System.out.println("Trying to 'mine' block 3... ");
        blockChain.get(2).miner(miningDepth);
        
        System.out.println("\nBlockchain is Valid: " + isHashCodeCheckValid());
        
        String blockchainJsond;
        blockchainJsond = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nThe block chain:");
        System.out.println(blockchainJsond);
        
        // TODO code application logic here
        Block baseBlock = new Block("This is the first block","0");
        System.out.println("SHA256 hash for block 1 : " + baseBlock.hashCode());
        Block secondBlock = new Block("This is the second block","0");
        System.out.println("SHA256 hash for block 2 : " + secondBlock.hashCode());
        Block thirdBlock = new Block("This is the third block","0");
        System.out.println("SHA256 hash for block 3 : " + thirdBlock.hashCode());
        blockChain.add(new Block("This is our first group block","0"));
        blockChain.add(new Block("This is our second group block", blockChain.get(blockChain.size()-1).newHashSignet));
        blockChain.add(new Block("This is our third group block", blockChain.get(blockChain.size()-1).newHashSignet));
        
        String blockChainJsond = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(blockChainJsond);
        
        
        //Setup Bouncey castle as a Security Provider
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
                
        //Create the new wallets
		WalletA = new WalletUtil();
		WalletB = new WalletUtil();
                
		//Test public and private keys
		System.out.println("Private and public keys:");
		System.out.println(SignatureUtil.getStringFromKey(WalletA.privateKeys));
		System.out.println(SignatureUtil.getStringFromKey(WalletB.publicKeys));
		//Create a test transaction from WalletA to walletB 
		TransactionsUtil transaction = new TransactionsUtil(WalletA.publicKeys, WalletB.publicKeys, 5, null);
		transaction.generateSignature(WalletA.privateKeys);
		//Verify the signature works and verify it from the public key
		System.out.println(transaction.verifySignature()+": Is signature verified? ");
                
                
       
        
        
    }
    
}
