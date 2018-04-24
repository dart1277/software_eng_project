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

        FileEncryptor enc = new FileEncryptor();

        enc.configure(key, CryptoModule.REGULAR_MODE, true, "Masełko pizdełko");
        try {
            enc.encrypt("C:\\Users\\wajda\\Desktop\\pypy\\ProgramSzyfrujaceDane.odt", "C:\\Users\\wajda\\Desktop\\pypy\\ProgramSzyfrujaceDane.odt");
            System.out.println(enc.getHelpMessage("C:/Users/wajda/Desktop/software_eng_project-master/software_eng_project-master/CopyFolder/software_eng_project.iml"));
            enc.decrypt("C:/Users/wajda/Desktop/software_eng_project-master/software_eng_project-master/CopyFolder/software_eng_project.iml", "C:/Users/wajda/Desktop/software_eng_project-master/software_eng_project-master/CopyFolder/software_eng_projectD.iml");

        } catch (IOException | CryptoException ex) {
            System.out.println("!" + ex.getMessage());
        }

        /*
        String key = "Dowolnej dlugosci kurwa jego mać zadziałą";
        File inputFile = new File("document.txt");
        File encryptedFile = new File("document_encrypted.jpg");
        File decryptedFile = new File("document_decrypted.jpg");

        ArrayList<String> lst = new ArrayList<>();
        lst.add("software_eng_project.iml");
        FileEncryptor enc = new FileEncryptor(lst);
        enc.configure(key, CryptoModule.REGULAR_MODE, true);
        try {
            enc.encryptNext();
        } catch (IOException | CryptoException ex) {
            System.out.println("!" + ex.getMessage());
        }


        ArrayList dlst = new ArrayList<String>();
        dlst.add("CopyFolder/software_eng_project.iml");
        FileEncryptor dec = new FileEncryptor(dlst);
        dec.configure(key, CryptoModule.REGULAR_MODE, true);
        try {
            dec.decryptNext();
        } catch (CryptoException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        */

    }
}

