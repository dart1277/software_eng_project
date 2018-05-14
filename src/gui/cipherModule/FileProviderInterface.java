package gui.cipherModule;

import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

public interface FileProviderInterface {

    /**
     * Returns next file from the list - therefore changing currently processed file
     *
     * @return next file from the list
     */
    File getNext();

    /**
     * Saves next bytes of currently processed file
     * @throws IOException IO Exception
     * @param bytesToSave encrypted byte array
     */
    void saveNextBytes(byte[] bytesToSave) throws IOException;

}
