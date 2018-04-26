package gui.cipherModule;

/**
 * Exception thrown by CryptoModule
 *
 * @author Paweł Talaga
 * @version 1.0
 * @since 2018-13-03
 */
public class CryptoException extends Exception {

    /**
     * Creates instance of CryptoException
     *
     * @param message   String message included in exception
     * @param throwable Throwable object the exception is built on
     */
    CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }

    CryptoException(String message) {
        super(message);
    }

    /**
     * Creates instance of CryptoException
     *
     * @param message  String message included in exception
     * @param filename String name of the file processed during ciphering process
     */
    CryptoException(String message, String filename) {
        super(message);
        this.filename = filename;
    }

    public String getFileName() {
        return this.filename;
    }

    private String filename;

}
