package gui.cipherModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//Tylko taki test czy działa
public class CryptoTest {
    public static void main(String[] args) {

        String key = "Dowolnej dlugosci kurwa jego mać zadziałą";
        File inputFile = new File("document.txt");
        File encryptedFile = new File("document_encrypted.jpg");
        File decryptedFile = new File("document_decrypted.jpg");

        ArrayList<String> lst = new ArrayList<>();
        lst.add("lmtry.vstsound");
        FileEncryptor enc = new FileEncryptor(lst);
        enc.configure(key, CryptoModule.REGULAR_MODE, true);
        try {
            enc.encryptNext();
        } catch (IOException | CryptoException | InvalidAlgorithmParameterException ex) {
            System.out.println("!" + ex.getMessage());
        }


        ArrayList dlst = new ArrayList<String>();
        dlst.add("CopyFolder/lmtry.vstsound");
        FileEncryptor dec = new FileEncryptor(dlst);
        dec.configure(key, CryptoModule.REGULAR_MODE, true);
        try {
            dec.decryptNext();
        } catch (CryptoException | IOException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex.getMessage());
        }


        /*
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(encryptedFile);
            FileOutputStream outputStream2 = new FileOutputStream(decryptedFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            byte[] encryptedBytes = new byte[(int) inputFile.length()];
            byte[] decryptedBytes = new byte[(int) inputFile.length()];

            inputStream.read(inputBytes);

            encryptedBytes = CryptoModule.encrypt(key, CryptoModule.REGULAR_MODE, inputBytes);
            outputStream.write(encryptedBytes);

            Header header = new Header("Pomocne hasło", "2", "101");
            encryptedBytes = ByteArrayUtils.addHeader(header, encryptedBytes);
            System.out.println(ByteArrayUtils.getHeader(encryptedBytes));
            encryptedBytes = ByteArrayUtils.removeHeader(encryptedBytes);

            decryptedBytes = CryptoModule.decrypt(key, CryptoModule.REGULAR_MODE, encryptedBytes);

            outputStream2.write(decryptedBytes);

            inputStream.close();
            outputStream.close();
            outputStream2.close();


        } catch (CryptoException | IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        */

    }
}

