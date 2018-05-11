package gui.cipherModule;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp()
    {
        encryptoTaskTest = new EncryptorTask(mock(FileEncryptor.class), mock(INotifier.class), mock(BiConsumer.class));
        decryptoTaskTest = new EncryptorTask(mock(FileEncryptor.class), mock(INotifier.class), mock(BiConsumer.class));
    }


    @Test
    public void add() {
        encryptoTaskTest.add("SRC", "DST");
        decryptoTaskTest.add("SRC", "DST");

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

        encryptoTaskTest.setLists(suc, fail);
        decryptoTaskTest.setLists(suc, fail);

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
}