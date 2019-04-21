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

    public  String newhash;
    public  String priorHash;
    private String message;
    private long timeStamp;
    
    public static ArrayList<Block> blockChain = new ArrayList<Block> ();
    
    public              Block(String data, String previousHash)
        {
            try
                {
                this.message = data;
                this.priorHash = previousHash;
                this.timeStamp = new Date().getTime();
                this.newhash = calculatedhash();
                }
            catch (UnsupportedEncodingException | RuntimeException | NoSuchAlgorithmException e)
                {
            
                };
        } 
    
    public final String calculatedhash() throws RuntimeException, NoSuchAlgorithmException, UnsupportedEncodingException 
        {
            String calculatedhash = signature.applySha256
            (
                priorHash + 
                Long.toString(timeStamp) + 
                message
            );
        
            return calculatedhash;
        };
    
    public static Boolean isHashCodeCheckValid()
        {
            Block currentBlock;
            Block previousBlock;
        
            return true;
        }
    
    
    
    /**
     * @param args the command line arguments
     * reference medium.com discussions
     */
    public static void main(String[] args) 
        {
        // TODO code application logic here
        //Block baseBlock = new Block("This is the first block","0");
        //System.out.println("SHA256 hash for block 1 : " + baseBlock.hashCode());
        
        //Block secondBlock = new Block("This is the second block","0");
        //System.out.println("SHA256 hash for block 2 : " + secondBlock.hashCode());
        
        //Block thirdBlock = new Block("This is the third block","0");
        //System.out.println("SHA256 hash for block 3 : " + thirdBlock.hashCode());
        
        blockChain.add(new Block("This is the first block","0"));
        blockChain.add(new Block("This is the second block", blockChain.get(blockChain.size()-1).newhash));
        blockChain.add(new Block("This is the third block", blockChain.get(blockChain.size()-1).newhash));
        
        String blockChainJsond = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(blockChainJsond);
        
    }
    
}
