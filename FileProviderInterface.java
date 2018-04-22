import java.io.File;

public interface FileProviderInterface
{

    /**
     * Returns next file from the list - therefore changing currently processed file
     *
     * @return next file from the list
     */
    File getNext();

    /**
     * Saves next bytes of currently processed file
     *
     * @param bytesToSave encrypted byte array
     *
     */
    void saveNextBytes(byte[] bytesToSave);

}
