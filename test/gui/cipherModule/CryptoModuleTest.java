package gui.cipherModule;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CryptoModuleTest {

    private  byte[] originalData;

    @Before
    public void setUp() throws Exception {
        originalData = "SomeData".getBytes();
    }

    @Test
    public void encrypt() throws CryptoException, InvalidAlgorithmParameterException {

        try {
            originalData = ByteArrayUtils.fillArrayToBlockSize(originalData,524288);
            byte[] encryptData = CryptoModule.encrypt("somekey",1,originalData);
            assertNotEquals(Arrays.toString(originalData),Arrays.toString(encryptData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decrypt() throws CryptoException, InvalidAlgorithmParameterException, BadPaddingException {
        try {
            byte[] originalData2 = ByteArrayUtils.fillArrayToBlockSize(originalData,524288);
            byte[] encryptData = CryptoModule.encrypt("somekey",1, originalData2);
            byte[] decryptData = CryptoModule.decrypt("somekey",1, encryptData);
           // decryptData = ByteArrayUtils.removePadding(decryptData,ByteArrayUtils.getHeader(encryptData).getPadding());
            assertEquals(Arrays.toString(originalData2),Arrays.toString(decryptData));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}