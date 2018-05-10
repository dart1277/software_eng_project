package gui.cipherModule;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.concurrent.Task;
import javafx.util.Pair;


/**
 * Abstract class for scheduling Tasks for ciphering processes to be
 * executed in background
 */
public abstract class CryptoTask extends Task {

    /**
     * CryptoTask constructor
     *
     * @param encryptor   FileEncryptor object used in ciphering processes
     * @param freezeGUI   interface used for freezing GUI form inside Task
     * @param unfreezeGUI functional interface used for unfreezing GUI form inside Task
     */
    public CryptoTask(FileEncryptor encryptor, INotifier freezeGUI, BiConsumer<List<String>, List<String>> unfreezeGUI) {
        this.processList = new ArrayList<>();
        this.encryptor = encryptor;
        this.freezeGUI = freezeGUI;
        this.unfreezeGUI = unfreezeGUI;
    }

    /**
     * Adds file paths to precessing list
     *
     * @param src String source file path
     * @param dst String destination file path
     */
    public void add(String src, String dst) {
        processList.add(new Pair<>(src, dst));
    }

    /**
     * Sets lists of paths for which process succeced and not if needed
     *
     * @param successList List of paths for which process ended in success
     * @param failedList  List of paths for which process ended in failure
     */
    public void setLists(List<String> successList, List<String> failedList) {
        this.succList = successList;
        this.failList = failedList;
    }

    /**
     * Overrides Task class method call() to be used in ciphering processes
     *
     * @return Void
     */
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


    /**
     * Starts ciphering processes Task
     */
    public void process() {
        new Thread(this).start();
    }


    /**
     * Abstract function calling proper method for encryption and decryption
     *
     * @param src String source filepath
     * @param dst String destination filepath
     * @throws IOException     when an error occurs during file IO operations
     * @throws CryptoException when an error occurs during ciphering process
     */
    protected abstract void methodCall(String src, String dst) throws IOException, CryptoException;


    private volatile ArrayList<Pair<String, String>> processList;
    private volatile List<String> succList;
    private volatile List<String> failList;
    protected FileEncryptor encryptor;
    private BiConsumer<List<String>, List<String>> unfreezeGUI;
    private INotifier freezeGUI;

}
