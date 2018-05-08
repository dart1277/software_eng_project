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

    public void setLists(ArrayList successList, ArrayList failedList){
        this.succList = successList;
        this.failList = failedList;
    }


    @Override
    public Void call(){
        for(Pair processed : this.processList){
            try {
                this.methodCall(processed.getKey().toString(), processed.getValue().toString());
                this.succList.add(processed.getKey().toString());
            }
            catch (IOException ex) {
                System.out.println("IO error");
                this.failList.add(processed.getKey().toString());
            } catch (CryptoException ex) {
                System.out.println("enc error");
                this.failList.add(processed.getKey().toString());
            }
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
    private volatile ArrayList<String> succList;
    private volatile ArrayList<String> failList;
    private FileEncryptor encryptor;

}
