package gui.cipherModule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileEncryptorTest {

    private static FileEncryptor fileEn;
    private FileEncryptor fileEn2;
    private static String basePath = new File("").getAbsolutePath();
    private static String pathToFile;
    private static byte[] originFileData;
    private static File originFile;
    private static FileInputStream fis;


    @BeforeClass
    public static void setUpClass() throws Exception {

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathToFile = "\\test\\gui\\cipherModule\\FileEncryptor_TestingFiles\\";
        }
        else {
            pathToFile = "/test/gui/cipherModule/FileEncryptor_TestingFiles/";
        }

        originFile = new File(basePath + pathToFile + "TestingFile1.txt");
        originFileData = new byte[(int)originFile.length()];
        fis = new FileInputStream(originFile);
        fis.read(originFileData);

    }

    @Before
    public void setUp() throws Exception
    {
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add("TestingFile1.txt");
        fileList.add("TestingFile2.txt");
        fileEn = new FileEncryptor(fileList, basePath + pathToFile);
        fileEn.configure("somekey",1,true,"helpMess");

    }

    @AfterClass
    public static void tearDownClass(){

        try {

            fis.close();

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void encrypt() throws IOException, CryptoException {
        //fileEn.encrypt(basePath + "/test/gui/cipherModule/TestingFile.txt",basePath + "/test/gui/cipherModule/EncFile.txt");

        fileEn.encrypt(basePath + pathToFile + "TestingFile1.txt",basePath + pathToFile + "EncFile1.txt");
        File EncFile = new File(basePath + pathToFile + "EncFile1.txt.chr");
        byte[] encFileData = new byte[(int)EncFile.length()];
        FileInputStream fis = new FileInputStream(EncFile);
        fis.read(encFileData);

        assertNotEquals(Arrays.toString(originFileData), Arrays.toString(encFileData));
    }

    @Test
    public void encryptException() throws IOException, CryptoException
    {
        try{

            fileEn.encrypt(basePath + pathToFile + "WrongTestingFile1.txt",basePath + pathToFile + "WrongEncFile1/");
            File EncFile = new File(basePath + pathToFile + "EncFile1.txt.chr");
            byte[] encFileData = new byte[(int)EncFile.length()];
            FileInputStream fis = new FileInputStream(EncFile);
            fis.read(encFileData);

            fail("Exception was not thrown!");
        }
        catch (CryptoException e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void decrypt()  throws IOException, CryptoException {

            fileEn.encrypt(basePath + pathToFile + "TestingFile1.txt", basePath + pathToFile + "EncFile1.txt");
            fileEn.decrypt(basePath + pathToFile + "EncFile1.txt.chr", basePath + pathToFile + "DycFile1.txt");

            File DycFile = new File(basePath + pathToFile + "DycFile1");
            byte[] dycFileData = new byte[(int) DycFile.length()];
            FileInputStream fis = new FileInputStream(DycFile);
            fis.read(dycFileData);

            assertEquals(Arrays.toString(originFileData), Arrays.toString(dycFileData));

    }

    @Test
    public void decryptException() throws IOException, CryptoException
    {
        try {
            fileEn.encrypt(basePath + pathToFile + "TestingFile1.txt", basePath + pathToFile + "EncFile1.txt");
            fileEn.decrypt(basePath + pathToFile + "WrongNameEncFile1.txt.chr", basePath + pathToFile + "DycFile1Except/");

            File DycFile = new File(basePath + pathToFile + "DycFile1");
            byte[] dycFileData = new byte[(int) DycFile.length()];
            FileInputStream fis = new FileInputStream(DycFile);
            fis.read(dycFileData);

            fail("Exception was not thrown!");
        }
        catch (CryptoException e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void configure() throws CryptoException {

        assertEquals("helpMess",fileEn.getHelpMessage(basePath + pathToFile + "EncFile1.txt.chr"));

        try {
            Field key = FileEncryptor.class.getDeclaredField("key");
            key.setAccessible(true);
            Field complexity = FileEncryptor.class.getDeclaredField("complexity");
            complexity.setAccessible(true);
            Field header = FileEncryptor.class.getDeclaredField("header");
            header.setAccessible(true);
            Field allowMultiEncryptions = FileEncryptor.class.getDeclaredField("allowMultiEncryptions");
            allowMultiEncryptions.setAccessible(true);

            assertEquals("somekey", key.get(fileEn));
            assertEquals(1, complexity.get(fileEn));
            assertEquals(true, allowMultiEncryptions.get(fileEn));

        }
        catch (NoSuchFieldException | IllegalAccessException e){

        }

    }

    @Test
    public void setKey(){
        try {
            Field field = FileEncryptor.class.getDeclaredField("key");
            field.setAccessible(true);

            fileEn.setKey("NewKey");
            assertEquals("NewKey", field.get(fileEn));

        } catch (NoSuchFieldException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setComplexity(){

        try {
            Field field = FileEncryptor.class.getDeclaredField("complexity");
            field.setAccessible(true);

            fileEn.setComplexity(3);
            assertEquals(3, field.get(fileEn));

        } catch (NoSuchFieldException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setMultiEncryptions(){

        try {
            Field field = FileEncryptor.class.getDeclaredField("allowMultiEncryptions");
            field.setAccessible(true);

            fileEn.setMultiEncryptions(false);
            assertFalse((Boolean) field.get(fileEn));

        } catch (NoSuchFieldException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setHelpMessage() throws CryptoException, IOException {
        fileEn.setHelpMessage("SetHelpMess");

        fileEn.encrypt(basePath + pathToFile + "TestingFile1.txt", basePath + pathToFile + "EncFile1ForSetHelp.txt");

        assertEquals("SetHelpMess", fileEn.getHelpMessage(basePath + pathToFile + "EncFile1ForSetHelp.txt.chr"));

    }
}