package gui.cipherModule;

public class HintMessageProvider {
    FileEncryptor encryptor = new FileEncryptor();

    public String getHint(String path, String defaultHint) {
        String hint = defaultHint;
        try {
            hint = encryptor.getHelpMessage(path);
        } catch (CryptoException e) {
            //e.printStackTrace();
        }
        return hint;
    }
}
