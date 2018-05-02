package gui.cipherModule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileEncryptorTest {

    private static FileEncryptor fileEn;
    private static String basePath = new File("").getAbsolutePath();
    private static byte[] originFileData;
    private static File originFile;

    @BeforeClass
    public static void setUp() throws Exception {
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add("TestingFile.txt");
        fileEn = new FileEncryptor(fileList, basePath + "/test/gui/cipherModule/");
        fileEn.configure("somekey",1,true,"helpMess");

        originFile = new File(basePath + "/test/gui/cipherModule/TestingFile.txt");
        originFileData = new byte[(int)originFile.length()];
        FileInputStream fis = new FileInputStream(originFile);
        fis.read(originFileData);

        fileEn.encrypt(basePath + "/test/gui/cipherModule/TestingFile.txt",basePath + "/test/gui/cipherModule/EncFile.txt");
    }

    @Test
    public void encrypt() throws IOException, CryptoException {
        //fileEn.encrypt(basePath + "/test/gui/cipherModule/TestingFile.txt",basePath + "/test/gui/cipherModule/EncFile.txt");

        File EncFile = new File(basePath + "/test/gui/cipherModule/EncFile.txt.chr");
        byte[] encFileData = new byte[(int)EncFile.length()];
        FileInputStream fis = new FileInputStream(EncFile);
        fis.read(encFileData);

        assertNotEquals(Arrays.toString(originFileData), Arrays.toString(encFileData));
    }

    @Test
    public void decrypt()  throws IOException, CryptoException {

        fileEn.decrypt(basePath + "/test/gui/cipherModule/EncFile.txt.chr",basePath + "/test/gui/cipherModule/DycFile.txt");

        File DycFile = new File(basePath + "/test/gui/cipherModule/DycFile");
        byte[] dycFileData = new byte[(int)DycFile.length()];
        FileInputStream fis = new FileInputStream(DycFile);
        fis.read(dycFileData);

        assertEquals(Arrays.toString(originFileData), Arrays.toString(dycFileData));
    }

    @Test
    public void configure() throws CryptoException {
        assertEquals("helpMess",fileEn.getHelpMessage(basePath + "/test/gui/cipherModule/EncFile.txt.chr"));
    }


}