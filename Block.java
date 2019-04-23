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

public class Block {

    public  String newHashSignet;
    public  String priorHashSignet;
    private final String message;
    private final long timeStamp;
    private int nonce;
    
    /**
     *
     */
    public static int miningDepth = 0x6;
    
    public static ArrayList<Block> blockChain = new ArrayList<Block> ();  
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
            
                this.message = data;
                this.priorHashSignet = previousHash;
                this.timeStamp = new Date().getTime();
                this.newHashSignet = calculatedhash();
                
        };
    
    public final String calculatedhash() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException 
        {
            String calculatedhash = SignatureUtil.applySha256
            (
                priorHashSignet + 
                Long.toString(timeStamp) + 
                Integer.toString(nonce) + 
                message
            );
        
            return calculatedhash;
        };
    
    public static Boolean isHashCodeCheckValid() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException
        {
            Block currentBlock;
            Block previousBlock;
            String hashTarget = new String(new char[miningDepth]).replace('\0','0');
            
        
            for(int i=1; i<blockChain.size(); i++)
                {
                    currentBlock = blockChain.get(i);
                    previousBlock = blockChain.get(i-1);
                    
                    //reclaculate and compare with the registered hash
                    if(!currentBlock.newHashSignet.equals(currentBlock.calculatedhash()))
                    {
                        System.out.println("Hash check error in current block");
                        return false;
                    }
                    
                    //compare previous hash and registered hash
                    if(!previousBlock.newHashSignet.equals(currentBlock.priorHashSignet))
                    {
                        System.out.println("Hash check error in previous block");
                        return false;
                    }
                }
            
            return true;
        };
    
    public void miner(int miningDepth) throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException
        {
            String mine = new String(new char[miningDepth]).replace('\0', '0');
            while (!newHashSignet.substring(0, miningDepth).equals(mine))
                {
                        nonce ++;
                        newHashSignet = calculatedhash();
                }
            
            System.out.println("New Block [" + newHashSignet + "] Succesfully Mined!!! : ");
        };
    
    /**
     * @param args the command line arguments
     * reference medium.com discussions
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, RuntimeException, NoSuchAlgorithmException 
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
        //Block baseBlock = new Block("This is the first block","0");
        //System.out.println("SHA256 hash for block 1 : " + baseBlock.hashCode());
        //Block secondBlock = new Block("This is the second block","0");
        //System.out.println("SHA256 hash for block 2 : " + secondBlock.hashCode());
        //Block thirdBlock = new Block("This is the third block","0");
        //System.out.println("SHA256 hash for block 3 : " + thirdBlock.hashCode());
        blockChain.add(new Block("This is the first block","0"));
        blockChain.add(new Block("This is the second block", blockChain.get(blockChain.size()-1).newHashSignet));
        blockChain.add(new Block("This is the third block", blockChain.get(blockChain.size()-1).newHashSignet));
        
        String blockChainJsond = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(blockChainJsond);
        
    }
    
}
