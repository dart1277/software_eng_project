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

public class FileProviderTest {

    private ArrayList<String> fileList = new ArrayList<>();
    private FileProvider prov;
    private static String basePath;
    private static String pathToFile;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        if (System.getProperty("os.name").startsWith("Windows")) {
            pathToFile = "\\test\\gui\\cipherModule\\FileProvider_TestingFiles\\";
        }
        else {
            pathToFile = "/test/gui/cipherModule/FileProvider_TestingFiles/";
        }
        basePath = new File("").getAbsolutePath();

    }

    @Before
    public void setUp() throws Exception {


        fileList.add("TestFile1.txt");
        //fileList.add("File2");
        prov = new FileProvider(fileList);

    }

    @Test
    public void FileProviderCtr(){
        assertEquals("TestFile1.txt",prov.getNext().getName());
    }

    @Test
    public void addNextPrimitive() {
        //This file TestFile2.txt does not exists
        assertTrue(prov.addNextPrimitive("TestFile2.txt","TestFile2.txt"));

        assertEquals("TestFile2.txt",prov.getNextPrimitiveNoStream().getName());

        //This file TestFile1.txt is already exists
        assertFalse(prov.addNextPrimitive(basePath + pathToFile + "TestFile1.txt", basePath + pathToFile + "TestFile1.txt"));
    }

    @Test
    public void hasNext() {
        assertTrue(prov.hasNext());

        File testFile = prov.getNext();
        assertNotNull(testFile);
        assertEquals("TestFile1.txt", testFile.getName());

        assertNull(prov.getNext());
    }

    @Test
    public void saveNextBytes()
    {
        byte[] toSave = "HelloWorld".getBytes();
        boolean isFileExist = prov.addNextPrimitive(basePath + pathToFile + "FileToSaveBytes.txt", basePath + pathToFile + "FileToSaveBytes.txt");
        if(!isFileExist)
        {
            prov.cleanBrokenDestination();
            prov.addNextPrimitive(basePath + pathToFile + "FileToSaveBytes.txt", basePath + pathToFile + "FileToSaveBytes.txt");
        }

        assertNotNull(prov.getNextPrimitive());

        prov.saveNextBytes(toSave);

        File savedFile = new File(basePath + pathToFile + "FileToSaveBytes.txt");
        byte[] savedFileData = new byte[(int)savedFile.length()];
        try {
            FileInputStream fis = new FileInputStream(savedFile);
            fis.read(savedFileData);
            fis.close();
        }
        catch (IOException e)
        {}

        assertEquals(Arrays.toString(toSave), Arrays.toString(savedFileData));
    }
}