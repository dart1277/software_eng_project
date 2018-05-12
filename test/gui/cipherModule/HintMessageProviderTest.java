package gui.cipherModule;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HintMessageProviderTest {

    private HintMessageProvider hintTest;

    @Before
    public void setUp() throws Exception {
        hintTest = new HintMessageProvider();
    }

    @Test
    public void HintCtr()
    {
        try {
            Field field = HintMessageProvider.class.getDeclaredField("encryptor");
            field.setAccessible(true);

            assertNotNull(field.get(hintTest));
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void getHint() throws CryptoException {

        FileEncryptor fileEn ;
        try {
            Field field = HintMessageProvider.class.getDeclaredField("encryptor");
            field.setAccessible(true);
            field.set(hintTest, mock(FileEncryptor.class));

            assertNotNull(field.get(hintTest));

            fileEn = (FileEncryptor) field.get(hintTest);
            when(fileEn.getHelpMessage("path")).thenReturn("HelpMess");
            when(fileEn.getHelpMessage("WrongPath")).thenThrow(CryptoException.class);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        String hintGetTest = hintTest.getHint("path","defMess");
        assertEquals("HelpMess", hintGetTest);


        hintGetTest = hintTest.getHint("WrongPath", "defMess");
        assertEquals("defMess", hintGetTest);

    }
}