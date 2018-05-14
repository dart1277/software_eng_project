package gui.cipherModule;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//Standard cipher names - don't even try other than AES and AES/ECB/PKCS5Padding, unless you want to feel pain.
//https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher
//https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory

/**
 * Class allowing encryption and decryption files
 * using Crypto package and Advanced Encryption Standard
 *
 * @author Paweł Talaga
 * @version 1.2
 * @since 2018-13-03
 */
public class CryptoModule {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/NoPadding";

    public static final int SLOW_MODE = 3;
    public static final int REGULAR_MODE = 2;
    public static final int FAST_MODE = 1;

    /**
     * Method allowing encryption of a byte array using Advanced Encryption Standard algorithm
     *
     * @param key        String password, constituting base for generating encryption key
     * @param complexity Integer complexity of AES meaning number of iterations
     *                   (affects encryption time complexity)
     *                   Can be used with inner static fields
     *                   SLOW_MODE, REGULAR_MODE, FAST_MODE of
     *                   values 3, 2 and 1 [iterations]
     * @param inputBytes array of bytes to cipher
     * @return ciphered array of bytes
     * @throws CryptoException when error in ciphering process occurs
     * @throws java.security.InvalidAlgorithmParameterException wrong algorithm provided
     */
    public static byte[] encrypt(String key, int complexity, byte[] inputBytes)
            throws CryptoException, InvalidAlgorithmParameterException {
        return doCrypto(Cipher.ENCRYPT_MODE, key, complexity, inputBytes);
    }

    /**
     * Method allowing decryption of a byte array using Advanced Encryption Standard algorithm
     *
     * @param key        String password, constituting base for generating decryption key
     * @param complexity Integer complexity of AES meaning number of iterations
     *                   (affects encryption time complexity)
     *                   Can be used with inner static fields
     *                   SLOW_MODE, REGULAR_MODE, FAST_MODE of
     *                   values 3, 2 and 1 [iterations]
     * @param inputBytes array of bytes to cipher
     * @return ciphered array of bytes
     * @throws CryptoException when error in ciphering process occurs
     * @throws java.security.InvalidAlgorithmParameterException wrong algorithm provided
     */
    public static byte[] decrypt(String key, int complexity, byte[] inputBytes)
            throws CryptoException, InvalidAlgorithmParameterException {
        return doCrypto(Cipher.DECRYPT_MODE, key, complexity, inputBytes);
    }

    /**
     * Method doing actual cryptographic operation
     *
     * @param cipherMode Integer mode passed to Cipher class
     *                   1 - encrypt mode
     *                   2 - decrypt mode
     * @param key        String password, constituting base for generating (en)decrpytion key
     * @param complexity Integer complexity of AES meaning number of iterations
     *                   (affects encryption time complexity)
     *                   Can be used with inner static fields
     *                   SLOW_MODE, REGULAR_MODE, FAST_MODE of
     *                   values 3, 2 and 1 [iterations]
     * @param inputBytes array of bytes to cipher
     * @return ciphered array of bytes
     * @throws CryptoException when error in ciphering process occurs
     */
    private static byte[] doCrypto(int cipherMode, String key, int complexity, byte[] inputBytes)
            throws CryptoException, InvalidAlgorithmParameterException {
        try {
            Cipher cipher = createCipherInstance(cipherMode, key, ALGORITHM, TRANSFORMATION);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            for (int k = 1; k < complexity; k++)
                outputBytes = cipher.doFinal(outputBytes);
            return outputBytes;
        } catch (BadPaddingException
                | IllegalBlockSizeException ex) {
            if (cipherMode == 1)
                throw new CryptoException("Error encrypting file " + ex.getMessage(), ex);
            else
                throw new CryptoException("Error decrypting file " + ex.getMessage(), ex);
        }
    }

    /**
     * Creates proper instance of Cipher class
     *
     * @param cipherMode    Integer mode passed to Cipher class
     *                      1 - encrypt mode
     *                      2 - decrypt mode
     * @param key           String password, constituting base for generating (en)decrpytion key
     * @param algorithmName Name of the key generation algorithm to use
     * @param transformName Name of the transformation algorithm to use
     * @return Cipher class instance
     * @throws CryptoException when error in ciphering process occurs
     */
    private static Cipher createCipherInstance(int cipherMode, String key,
                                               String algorithmName, String transformName)
            throws CryptoException, InvalidAlgorithmParameterException {
        try {
            byte[] keyBytes = KeyStringRefiner.refine(key);
            Key secretKey = new SecretKeySpec(keyBytes, algorithmName);
            Cipher cipher = Cipher.getInstance(transformName);
            byte[] iv = {1, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 39, 41, 43, 47};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(cipherMode, secretKey, ivspec);
            return cipher;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException
                | InvalidKeyException ex) {
            if (cipherMode == 1)
                throw new CryptoException("Error encrypting file " + ex.getMessage(), ex);
            else
                throw new CryptoException("Error decrypting file " + ex.getMessage(), ex);
        }
    }
}
