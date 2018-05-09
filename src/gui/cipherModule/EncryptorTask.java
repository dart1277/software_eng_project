package gui.cipherModule;


//Na razie szkielet klasy do wątków

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.concurrent.Task;
import javafx.util.Pair;


public class EncryptorTask extends Task {


    public EncryptorTask(FileEncryptor encryptor, int mode, INotifier freezeGUI, BiConsumer<List<String>, List<String>> unfreezeGUI) {
        this.processList = new ArrayList<>();
        this.encryptor = encryptor;
        this.mode = mode;
        this.freezeGUI = freezeGUI;
        this.unfreezeGUI = unfreezeGUI;
    }


    public void add(String src, String dst) {
        processList.add(new Pair<>(src, dst));
    }

    public void setLists(List<String> successList, List<String> failedList) {
        this.succList = successList;
        this.failList = failedList;
    }


    @Override
    public Void call() {
        freezeGUI.notifies();
        for (Pair processed : this.processList) {
            try {
                this.methodCall(processed.getKey().toString(), processed.getValue().toString());
                this.succList.add(processed.getKey().toString());
            } catch (IOException ex) {
                System.out.println("IO error");
                this.failList.add(processed.getKey().toString());
            } catch (CryptoException ex) {
                System.out.println("enc error");
                this.failList.add(processed.getKey().toString());
            }
        }
        unfreezeGUI.accept(succList, failList);
        return null;
    }


    public void process() {
        new Thread(this).start();
    }


    private void methodCall(String src, String dst) throws IOException, CryptoException {
        if (this.mode == 0)
            this.encryptor.encrypt(src, dst);
        else
            this.encryptor.decrypt(src, dst);
    }

    public static final int ENCRYPT_MODE = 0;
    public static final int DECRYPT_MODE = 1;

    private int mode;
    private Task<Void> processingTask;
    private volatile ArrayList<Pair<String, String>> processList;
    private volatile List<String> succList;
    private volatile List<String> failList;
    private FileEncryptor encryptor;
    private BiConsumer<List<String>, List<String>> unfreezeGUI;
    private INotifier freezeGUI;

}
