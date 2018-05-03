package gui.cipherModule;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ByteArrayUtilsTest {

    private static byte[] byteArr;
    private static Header head;
    private static byte[] withHeader;

    @BeforeClass
    public static void setUp() throws Exception {
        byteArr = "SomeData".getBytes();
        head = new Header("help","1","000000");
        withHeader = ByteArrayUtils.addHeader(head,byteArr);
    }

    @Test
    public void addHeader() throws UnsupportedEncodingException, CryptoException  {

       assertNotEquals(byteArr, withHeader);
       assertNotNull(ByteArrayUtils.getHeader(withHeader));
       assertEquals(head,ByteArrayUtils.getHeader(withHeader));//Problem
    }

    @Test
    public void removeHeader()  throws UnsupportedEncodingException, CryptoException  {


        byte[] withoutHeader = ByteArrayUtils.removeHeader(withHeader);

        assertEquals(Arrays.toString(byteArr),Arrays.toString(withoutHeader));//Problem

        withoutHeader = ByteArrayUtils.addHeader(head,withoutHeader);
        withoutHeader = ByteArrayUtils.removeHeader(withoutHeader);
        assertEquals(Arrays.toString(byteArr),Arrays.toString(withoutHeader));

        try {
            byte[] withoutHeader2 = ByteArrayUtils.removeHeader(withoutHeader);
            fail("Excetion was not thrown!");
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("withoutHeader  does not have header to remove!");
        }
    }

    @Test
    public void hasCipheredDenotation()   {

        assertTrue(ByteArrayUtils.hasCipheredDenotation(withHeader));

    }

    @Test
    public void getHeaderBytesSize() throws CryptoException, UnsupportedEncodingException {
        assertEquals(withHeader.length - byteArr.length, ByteArrayUtils.getHeaderBytesSize(head));
        
    }

    @Test
    public void fillArrayToBlockSize() throws IOException {
        byte[] toFill = ByteArrayUtils.fillArrayToBlockSize(withHeader,524288);

        assertEquals(524288,toFill.length);

    }

    @Test
    public void removePadding() throws IOException, BadPaddingException {
        byte[] toFill = ByteArrayUtils.fillArrayToBlockSize(withHeader,524288);
        byte[] withoutPadding = ByteArrayUtils.removePadding(toFill,524288 - withHeader.length);

        assertEquals(Arrays.toString(withHeader), Arrays.toString(withoutPadding));
    }
}