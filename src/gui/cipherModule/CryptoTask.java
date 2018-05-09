package gui.cipherModule;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.concurrent.Task;
import javafx.util.Pair;


public abstract class CryptoTask extends Task {


    public CryptoTask(FileEncryptor encryptor, INotifier freezeGUI, BiConsumer<List<String>, List<String>> unfreezeGUI) {
        this.processList = new ArrayList<>();
        this.encryptor = encryptor;
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


    protected abstract void methodCall(String src, String dst) throws IOException, CryptoException;


    private volatile ArrayList<Pair<String, String>> processList;
    private volatile List<String> succList;
    private volatile List<String> failList;
    protected FileEncryptor encryptor;
    private BiConsumer<List<String>, List<String>> unfreezeGUI;
    private INotifier freezeGUI;

}
