
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
     * @param message String message included in exception
     * @param throwable Throwable object the exception is built on
     */
    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CryptoException(String message){
        super(message);
    }

}
