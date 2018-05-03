package gui.cipherModule;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CryptoModuleTest {

    private  byte[] originalData;
    private Method doCryptoMethod;

    @Before
    public void setUp() throws Exception {
        originalData = "SomeData".getBytes();

        doCryptoMethod = CryptoModule.class.getDeclaredMethod("doCrypto", Integer.TYPE, String.class, Integer.TYPE, byte[].class);
        doCryptoMethod.setAccessible(true);

    }

    @Test
    public void encrypt() throws CryptoException, InvalidAlgorithmParameterException {

        try {

            originalData = ByteArrayUtils.fillArrayToBlockSize(originalData,524288);

            byte[] encryptData = CryptoModule.encrypt("somekey",1,originalData);
            byte[] doEnrypt = (byte[]) doCryptoMethod.invoke(null, 1, "somekey", 1, originalData);

            assertNotEquals(Arrays.toString(originalData),Arrays.toString(encryptData));
            assertEquals(Arrays.toString(doEnrypt), Arrays.toString(encryptData));

        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decrypt() throws CryptoException, InvalidAlgorithmParameterException, BadPaddingException {
        try {
            byte[] originalData2 = ByteArrayUtils.fillArrayToBlockSize(originalData,524288);

            byte[] encryptData = CryptoModule.encrypt("somekey", 1, originalData2);
            byte[] decryptData = CryptoModule.decrypt("somekey", 1, encryptData);
            byte[] doDecrypt = (byte[]) doCryptoMethod.invoke(null, 2, "somekey", 1, encryptData);

            assertEquals(Arrays.toString(originalData2), Arrays.toString(decryptData));
            assertEquals(Arrays.toString(decryptData), Arrays.toString(doDecrypt));
            assertNotEquals(Arrays.toString(encryptData), Arrays.toString(decryptData));

        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = CryptoException.class)
    public void doCryptoException() throws CryptoException, InvalidAlgorithmParameterException, BadPaddingException {

            //CryptoException should be thrown
            byte[] encryptData = CryptoModule.encrypt("somekey", 1, originalData);
            byte[] decryptData = CryptoModule.decrypt("somekey", 1, encryptData);

    }

    @Test
    public void createCipherInstance(){

        try {
            //Object cryptoModule = new CryptoModule();
            Method method = CryptoModule.class.getDeclaredMethod("createCipherInstance", Integer.TYPE, String.class, String.class, String.class);
            method.setAccessible(true);

            Object obj1 = method.invoke(null, 1, "somekey", "AES", "AES/CBC/NoPadding");
            assertNotNull(obj1);

            Object obj2 = method.invoke(null, 1, "somekey", "WrongAlgorytm", "AES/CBC/NoPadding");
            fail("Exception because of AlgorithmName was not thrown!");
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            System.out.println("ERROR WrongAlgorith" + e.getClass());
        }
    }
}