/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bydlt;
import java.security.*;
import java.security.spec.*;

/**
 *
 * @author user
 */
public class WalletUtil {
    
        public PrivateKey privateKeys;
        public PublicKey publicKeys;
        
        public WalletUtil()  {
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
}
