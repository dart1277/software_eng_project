package gui.cipherModule;


//Na razie szkielet klasy do wątków

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.concurrent.Task;
import javafx.util.Pair;


public class EncryptorTask extends CryptoTask {


    public EncryptorTask(FileEncryptor encryptor, INotifier freezeGUI, BiConsumer<List<String>, List<String>> unfreezeGUI) {
        super(encryptor, freezeGUI, unfreezeGUI);
    }

    protected void methodCall(String src, String dst) throws IOException, CryptoException {
        this.encryptor.encrypt(src, dst);
    }

}