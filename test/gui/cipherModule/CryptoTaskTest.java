package gui.cipherModule;

import javafx.util.Pair;
import javafx.concurrent.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CryptoTaskTest {

    private CryptoTask encryptoTaskTest;
    private CryptoTask decryptoTaskTest;

    @Mock
    private INotifier freezTest;
    @Mock
    private BiConsumer unfreezTest;
    @Mock
    private FileEncryptor fileEn;
    @Before
    public void setUp()
    {
        freezTest = mock(INotifier.class);
        unfreezTest = mock(BiConsumer.class);
        fileEn = mock(FileEncryptor.class);

        encryptoTaskTest = new EncryptorTask(fileEn, freezTest, unfreezTest);
        decryptoTaskTest = new EncryptorTask(fileEn,freezTest, unfreezTest);

        encryptoTaskTest.add("SRC", "DST");
        decryptoTaskTest.add("SRC", "DST");

        List<String> suc = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        suc.add("OK");
        fail.add("Not OK");

        encryptoTaskTest.setLists(suc, fail);
        decryptoTaskTest.setLists(suc, fail);


    }


    @Test
    public void add() {

        ArrayList<Pair<String, String>> testList = new ArrayList<>();
        testList.add(new Pair<>("SRC", "DST"));

        try {
            Field field = CryptoTask.class.getDeclaredField("processList");
            field.setAccessible(true);

            assertEquals(testList, field.get(encryptoTaskTest));
            assertEquals(testList, field.get(decryptoTaskTest));
        }
        catch (NoSuchFieldException | IllegalAccessException  e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void setLists() {

        List<String> suc = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        suc.add("OK");
        fail.add("Not OK");

        try {
            Field fieldSuc = CryptoTask.class.getDeclaredField("succList");
            Field fieldFail = CryptoTask.class.getDeclaredField("failList");
            fieldFail.setAccessible(true);
            fieldSuc.setAccessible(true);

            assertEquals(suc, fieldSuc.get(encryptoTaskTest));
            assertEquals(fail, fieldFail.get(encryptoTaskTest));

            assertEquals(suc, fieldSuc.get(decryptoTaskTest));
            assertEquals(fail, fieldFail.get(decryptoTaskTest));
        }
        catch (NoSuchFieldException | IllegalAccessException  e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void methodCall() throws IOException, CryptoException {

        try
        {
            Method method = CryptoTask.class.getDeclaredMethod("methodCall", String.class, String.class);
            method.setAccessible(true);

            Object obj1 = method.invoke(encryptoTaskTest, "SRC", "DST");

            Field field = CryptoTask.class.getDeclaredField("encryptor");
            field.setAccessible(true);

            FileEncryptor fe = (FileEncryptor) field.get(encryptoTaskTest);

            verify(fe).encrypt("SRC", "DST");
        }
        catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void call() throws IOException, CryptoException {
        List<String> suc = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        suc.add("OK");
        suc.add("SRC");
        fail.add("Not OK");

        assertNull(encryptoTaskTest.call());

        verify(freezTest).notifies();
        verify(unfreezTest).accept(suc, fail);

        try {
            Field fieldSuc = CryptoTask.class.getDeclaredField("succList");
            Field fieldFail = CryptoTask.class.getDeclaredField("failList");
            fieldSuc.setAccessible(true);
            fieldFail.setAccessible(true);

            assertEquals(suc, fieldSuc.get(encryptoTaskTest));
            assertEquals(fail, fieldFail.get(encryptoTaskTest));

        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void callException() throws IOException, CryptoException
    {

        List<String> suc = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        suc.add("OK");
        //suc.add("SRC");
        fail.add("Not OK");

        try {
            Field fieldSuc = CryptoTask.class.getDeclaredField("succList");
            Field fieldFail = CryptoTask.class.getDeclaredField("failList");
            fieldSuc.setAccessible(true);
            fieldFail.setAccessible(true);

            doThrow(new IOException()).when(fileEn).encrypt("SRC", "DST");
            encryptoTaskTest.call();
            fail.add("SRC");
            assertEquals(suc, fieldSuc.get(encryptoTaskTest));
            assertEquals(fail, fieldFail.get(encryptoTaskTest));

            doThrow(new CryptoException("")).when(fileEn).encrypt("SRC", "DST");
            encryptoTaskTest.call();
            fail.add("SRC");
            assertEquals(suc, fieldSuc.get(encryptoTaskTest));
            assertEquals(fail, fieldFail.get(encryptoTaskTest));

        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }

}