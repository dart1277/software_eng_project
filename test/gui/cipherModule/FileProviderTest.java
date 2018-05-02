package gui.cipherModule;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FileProviderTest {

    private ArrayList<String> fileList = new ArrayList<>();
    private FileProvider prov;

    @Before
    public void setUp() throws Exception {
        fileList.add("File1");
        //fileList.add("File2");
        prov = new FileProvider(fileList);
    }

    @Test
    public void FileProviderCtr(){
        assertEquals("File1",prov.getNext().getName());
    }

    @Test
    public void addNextPrimitive() {
        prov.addNextPrimitive("File3","TestFile2");

        assertEquals("File3",prov.getNextPrimitiveNoStream().getName());
    }

    @Test
    public void hasNext() {
        assertTrue(prov.hasNext());
        assertNotNull(prov.getNext());
        assertNull(prov.getNext());
    }
}