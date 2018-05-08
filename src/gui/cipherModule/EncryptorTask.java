package gui.cipherModule;


//Na razie szkielet klasy do wątków

import java.io.IOException;
import java.util.ArrayList;
import javafx.concurrent.Task;
import javafx.util.Pair;


public class EncryptorTask extends Task{


    public EncryptorTask(FileEncryptor encryptor, int mode){
        this.processList = new ArrayList<>();
        this.encryptor = encryptor;
        this.mode = mode;
    }


    public void add(String src, String dst){
        processList.add(new Pair<>(src, dst));
    }


    @Override
    public Void call() throws IOException, CryptoException{
        for(Pair processed : this.processList){
            this.methodCall(processed.getKey().toString(), processed.getValue().toString());
        }
        return null;
    }


    public void process(){
        new Thread(this).start();
    }


    private void methodCall(String src, String dst) throws IOException, CryptoException{
        if(this.mode == 0)
            this.encryptor.encrypt(src, dst);
        else
            this.encryptor.decrypt(src, dst);
    }

    public static final int ENCRYPT_MODE = 0;
    public static final int DECRYPT_MODE = 1;

    private int mode;
    private Task<Void> processingTask;
    private volatile ArrayList<Pair<String, String>> processList;
    private FileEncryptor encryptor;

}
