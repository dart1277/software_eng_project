package gui.cipherModule;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;

/**
 * Class allowing file encryption, processing files from given list one by one
 *
 * @author Paweł Talaga
 * @version 1.4
 * @since 2018-18-04
 *
 */
public class FileEncryptor {


    /**
     * Not safe, use only with encrypt(String, String), decrypt(String, String) and getHelpMessage(String)
     * methods, any other usage can result with runtime errors
     *
     */
    public FileEncryptor(){
        this.allowStandardMethodAccess = false;
        this.bufferedFile = null;
        ArrayList<String> arr = new ArrayList<>();
        this.fileProvider = new FileProvider(arr, "");
        this.key = FileEncryptor.defaultKey;
        this.complexity = FileEncryptor.defaultComplexity;
        this.allowMultiEncryptions = true;
    }


    /**
     * Creates instance of FileEncryptor for given array of file paths to process
     *
     * @param filePathArray String ArrayList of file paths to process
     */
    public FileEncryptor(ArrayList<String> filePathArray) {
        this.fileProvider = new FileProvider(filePathArray);
        this.bufferedFile = null;
        this.key = FileEncryptor.defaultKey;
        this.complexity = FileEncryptor.defaultComplexity;
        this.allowMultiEncryptions = true;
    }


    /**
     * Creates instance of FileEncryptor for given array of file paths to process
     * and given destination directory name/path
     * given as 'DirName/' not a 'DirName'
     *
     * @param filePathArray String ArrayList of file paths to process
     * @param destDirName String being destination directory name/path
     *
     */
    public FileEncryptor(ArrayList<String> filePathArray, String destDirName){
        this.fileProvider = new FileProvider(filePathArray, destDirName);
        this.bufferedFile = null;
        this.key = FileEncryptor.defaultKey;
        this.complexity = FileEncryptor.defaultComplexity;
        this.allowMultiEncryptions = true;
    }


    /**
     * Used to configure ciphering password, algorithm complexity and denote if multiencryption is allowed
     *
     * @param key String password to use
     * @param complexity Integer complexity of AES meaning number of iterations
     *                   (affects encryption time complexity)
     *                   Can be used with inner static fields
     *                   SLOW_MODE, REGULAR_MODE, FAST_MODE of CryptoModule
     * @param allowMultiEncryptions boolean denoting if multiencryption is allowed
     */
    public void configure(String key, Integer complexity, boolean allowMultiEncryptions) {
        this.configure(key, complexity, allowMultiEncryptions, "");
    }


    /**
     * Used to configure ciphering password, algorithm complexity and denote if multiencryption is allowed,
     * and message added to header during encryption process
     *
     * @param key String password to use
     * @param complexity Integer complexity of AES meaning number of iterations
     *                   (affects encryption time complexity)
     *                   Can be used with inner static fields
     *                   SLOW_MODE, REGULAR_MODE, FAST_MODE of CryptoModule
     * @param allowMultiEncryptions boolean denoting if multiencryption is allowed
     * @param helpMessage message to attach to added header
     */
    public void configure(String key, Integer complexity,
                          boolean allowMultiEncryptions, String helpMessage) {
        this.key = key;
        this.complexity = complexity;
        this.header = new Header(helpMessage, complexity.toString(), "000000");
        this.allowMultiEncryptions = allowMultiEncryptions;
    }


    /**
     * Sets the password required to process in file ciphering
     *
     * @param key String password used in ciphering process
     */
    public void setKey(String key){
        this.key = key;
    }


    /**
     * Sets the algorithm complexity
     *
     * @param complexity Integer parameter in range from 1 to 3
     *                   Can be marked as CryptoModule.FAST_MODE
     *                                                .REGULAR_MODE
     *                                                .SLOW_MODE
     */
    public void setComplexity(Integer complexity){
        this.complexity = complexity;
        this.header.setComplexity(complexity);
    }


    /**
     * Sets denotation whether already encrypted file can be encrypted another time
     * (has nothing to do with complexity, because complexity affects lower level CryptoModule
     * class, which operates on raw byte arrays, without checking headers, for what upper level
     * classes are responsible
     *
     * @param allowMultiEncryptions boolean value denoting if multiple encryption is allowed for files
     */
    public void setMultiEncryptions(boolean allowMultiEncryptions){
        this.allowMultiEncryptions = allowMultiEncryptions;
    }


    /**
     * Sets message to attach to header, after that it can be retrieved with getHelpMessage from the file,
     *
     * @param helpMessage String being the message to attach
     */
    public void setHelpMessage(String helpMessage){
        this.header.setHelpMessage(helpMessage);
    }


    /**
     * Returns true if there are any files left to process
     *
     * @return boolean value denoting whether there are still files to process
     */
    public boolean hasNext(){
        return fileProvider.hasNext();
    }


    /**
     * Returns message attached to header for next file in the list
     *
     * @return String being the message attached to next files header
     *
     * @throws CryptoException containing information on which file caused exception
     *                         (use CryptoException's getFileName() method)
     */
    public String getHelpMessage() throws CryptoException{
        if(this.allowStandardMethodAccess) {
            if (this.fileProvider.hasNext())
                this.bufferedFile = fileProvider.getNext();
            try {
                FileInputStream stream = new FileInputStream(this.bufferedFile);
                byte[] headingArray = new byte[FileEncryptor.headingBlockSize];
                stream.read(headingArray, 0, FileEncryptor.headingBlockSize);
                return ByteArrayUtils.getHeader(headingArray).getHelpMessage();
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), this.bufferedFile.getName());
            }
        }
        else{
            System.out.println("Standard method access blocked due to empty constructor usage");
            return "";
        }
    }


    /**
     * Returns help message for specific filepath
     *
     * @param path path to get message from
     *
     * @return String message contained in file's header
     *
     * @throws CryptoException when something goes wrong.
     */
    public String getHelpMessage(String path) throws CryptoException{
        if(!this.allowStandardMethodAccess) {
            fileProvider.addNextPrimitive(path, "");
            File msgBuffer = fileProvider.getNextPrimitive();
            try {
                FileInputStream stream = new FileInputStream(msgBuffer);
                byte[] headingArray = new byte[FileEncryptor.headingBlockSize];
                stream.read(headingArray, 0, FileEncryptor.headingBlockSize);
                return ByteArrayUtils.getHeader(headingArray).getHelpMessage();
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), msgBuffer.getName());
            }
        }
        else{
            System.out.println("Can access standard method getHelpMessage() only - use empty constructor to access this");
            return "";
        }
    }


    /**
     * Encrypts next file from the list
     *
     * @throws IOException when file is nonexistent, it's not users fault to choose nonexistent file
     *                     because he shouldn't can do it using GUI
     * @throws CryptoException the only one you should worry about, contains file name
     *                          (use getFileName method to get file for which error has occured);
     */
    public void encryptNext()
            throws IOException, CryptoException{
        if(this.allowStandardMethodAccess) {
            FileInputStream currIn = null;
            File current;
            if (this.bufferedFile == null)
                current = this.fileProvider.getNext();
            else
                current = this.bufferedFile;
            try {
                currIn = new FileInputStream(current);
                long remainingBytes = current.length();
                long currentBytes;
                int paddingSize = ByteArrayUtils.getPaddingSize(blockSize, current.length());
                this.header.setPadding(paddingSize);

                if (remainingBytes >= FileEncryptor.blockSize) {
                    remainingBytes -= FileEncryptor.blockSize;
                    currentBytes = FileEncryptor.blockSize;
                } else {
                    currentBytes = remainingBytes;
                    remainingBytes = 0;
                }
                byte[] arrayToCipher = new byte[(int) currentBytes];
                currIn.read(arrayToCipher, 0, (int) currentBytes);

                boolean proceedEncryption = ((this.allowMultiEncryptions) ||
                        (!ByteArrayUtils.hasCipheredDenotation(arrayToCipher)));

                if (proceedEncryption) {

                    byte[] headingBlock = ByteArrayUtils.createHeadingBlock(this.header, headingBlockSize);

                    System.out.println("ENCRYPTION");
                    System.out.println(headingBlock.length);
                    System.out.println(this.header);

                    this.fileProvider.saveNextBytes(headingBlock);
                    if (remainingBytes == 0)
                        arrayToCipher = ByteArrayUtils.fillArrayToBlockSize(arrayToCipher, FileEncryptor.blockSize);
                    arrayToCipher = CryptoModule.encrypt(this.key, this.complexity, arrayToCipher);
                    this.fileProvider.saveNextBytes(arrayToCipher);

                    while (remainingBytes > 0) {
                        if (remainingBytes >= FileEncryptor.blockSize) {
                            remainingBytes -= FileEncryptor.blockSize;
                            currentBytes = FileEncryptor.blockSize;
                        } else {
                            currentBytes = remainingBytes;
                            remainingBytes = 0;
                        }
                        arrayToCipher = new byte[(int) currentBytes];
                        currIn.read(arrayToCipher, 0, (int) currentBytes);
                        if (remainingBytes == 0)
                            arrayToCipher = ByteArrayUtils.fillArrayToBlockSize(arrayToCipher, FileEncryptor.blockSize);
                        arrayToCipher = CryptoModule.encrypt(this.key, this.complexity, arrayToCipher);
                        this.fileProvider.saveNextBytes(arrayToCipher);
                    }
                }
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), current.getName());
            } finally {
                if (currIn != null) currIn.close();
                fileProvider.closeOutputFile();
                this.bufferedFile = null;
            }
        }
        else{
            System.out.println("Standard method access blocked due to empty constructor usage");
        }
    }


    /**
     * Decrypts next file from the list
     *
     * @throws IOException when file is nonexistent, it's not users fault to choose nonexistent file
     *                     because he shouldn't can do it using GUI
     * @throws CryptoException the only one you should worry about, contains file name
     *                          (use getFileName method to get file for which error has occured);
     */
    public void decryptNext()
            throws IOException, CryptoException{
        if(this.allowStandardMethodAccess) {
            File current;
            if (this.bufferedFile == null)
                current = this.fileProvider.getNext();
            else
                current = this.bufferedFile;
            FileInputStream currIn;
            try {
                currIn = new FileInputStream(current);
                long remainingBytes = current.length();
                int currentBytes;

                currentBytes = FileEncryptor.headingBlockSize;

                byte[] arrayToCipher = new byte[currentBytes];

                remainingBytes -= currentBytes;

                currIn.read(arrayToCipher, 0, currentBytes);

                System.out.println("DECRYPTION");
                System.out.println(arrayToCipher.length);
                System.out.println(ByteArrayUtils.getHeader(arrayToCipher));

                boolean proceedDecryption = ByteArrayUtils.hasCipheredDenotation(arrayToCipher) &&
                        ByteArrayUtils.getHeader(arrayToCipher).getComplexity().equals(this.complexity.toString());


                int paddingSize = ByteArrayUtils.getHeader(arrayToCipher).getPadding();

                if (proceedDecryption) {

                    while (remainingBytes > 0) {
                        if (remainingBytes >= FileEncryptor.blockSize) {
                            remainingBytes -= FileEncryptor.blockSize;
                            currentBytes = FileEncryptor.blockSize;
                        } else {
                            currentBytes = (int) remainingBytes;
                            remainingBytes = 0;
                        }
                        arrayToCipher = new byte[currentBytes];
                        currIn.read(arrayToCipher, 0, currentBytes);
                        arrayToCipher = CryptoModule.decrypt(this.key, this.complexity, arrayToCipher);
                        if (remainingBytes == 0)
                            arrayToCipher = ByteArrayUtils.removePadding(arrayToCipher, paddingSize);
                        this.fileProvider.saveNextBytes(arrayToCipher);
                    }
                }
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), current.getName());
            }
            currIn.close();
            fileProvider.closeOutputFile();
            this.bufferedFile = null;
        }
        else{
            System.out.println("Standard method access blocked due to empty constructor usage");
        }
    }


    /**
     * Encrypts file from source and saves it to destination
     *
     * @throws IOException when file is nonexistent, it's not users fault to choose nonexistent file
     *                     because he shouldn't can do it using GUI
     * @throws CryptoException the only one you should worry about, contains file name
     *                          (use getFileName method to get file for which error has occured);
     */
    public void encrypt(String source, String destination)
            throws IOException, CryptoException{
        if(!this.allowStandardMethodAccess) {
            FileInputStream currIn = null;
            File current;
            this.fileProvider.addNextPrimitive(source, destination);
            current = this.fileProvider.getNextPrimitive();
            try {
                currIn = new FileInputStream(current);
                long remainingBytes = current.length();
                long currentBytes;
                int paddingSize = ByteArrayUtils.getPaddingSize(blockSize, current.length());
                this.header.setPadding(paddingSize);

                if (remainingBytes >= FileEncryptor.blockSize) {
                    remainingBytes -= FileEncryptor.blockSize;
                    currentBytes = FileEncryptor.blockSize;
                } else {
                    currentBytes = remainingBytes;
                    remainingBytes = 0;
                }
                byte[] arrayToCipher = new byte[(int) currentBytes];
                currIn.read(arrayToCipher, 0, (int) currentBytes);

                boolean proceedEncryption = ((this.allowMultiEncryptions) ||
                        (!ByteArrayUtils.hasCipheredDenotation(arrayToCipher)));

                if (proceedEncryption) {

                    byte[] headingBlock = ByteArrayUtils.createHeadingBlock(this.header, headingBlockSize);

                    System.out.println("ENCRYPTION");
                    System.out.println(headingBlock.length);
                    System.out.println(this.header);

                    this.fileProvider.saveNextBytes(headingBlock);
                    if (remainingBytes == 0)
                        arrayToCipher = ByteArrayUtils.fillArrayToBlockSize(arrayToCipher, FileEncryptor.blockSize);
                    arrayToCipher = CryptoModule.encrypt(this.key, this.complexity, arrayToCipher);
                    this.fileProvider.saveNextBytes(arrayToCipher);

                    while (remainingBytes > 0) {
                        if (remainingBytes >= FileEncryptor.blockSize) {
                            remainingBytes -= FileEncryptor.blockSize;
                            currentBytes = FileEncryptor.blockSize;
                        } else {
                            currentBytes = remainingBytes;
                            remainingBytes = 0;
                        }
                        arrayToCipher = new byte[(int) currentBytes];
                        currIn.read(arrayToCipher, 0, (int) currentBytes);
                        if (remainingBytes == 0)
                            arrayToCipher = ByteArrayUtils.fillArrayToBlockSize(arrayToCipher, FileEncryptor.blockSize);
                        arrayToCipher = CryptoModule.encrypt(this.key, this.complexity, arrayToCipher);
                        this.fileProvider.saveNextBytes(arrayToCipher);
                    }
                }
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), current.getName());
            } finally {
                if (currIn != null) currIn.close();
                this.bufferedFile = null;
            }
        }
        else{
            System.out.println("Can use only standard method encryptNext() - use empty constructor to access this");
        }
    }


    /**
     * Decrypts next file from source and saves it to destination
     *
     * @throws IOException when file is nonexistent, it's not users fault to choose nonexistent file
     *                     because he shouldn't can do it using GUI
     * @throws CryptoException the only one you should worry about, contains file name
     *                          (use getFileName method to get file for which error has occured);
     */
    public void decrypt(String source, String destination)
            throws IOException, CryptoException{
        if(!this.allowStandardMethodAccess) {
            File current;
            this.fileProvider.addNextPrimitive(source, destination);
            current = this.fileProvider.getNextPrimitive();

            FileInputStream currIn;
            try {
                currIn = new FileInputStream(current);
                long remainingBytes = current.length();
                int currentBytes;

                currentBytes = FileEncryptor.headingBlockSize;

                byte[] arrayToCipher = new byte[currentBytes];

                remainingBytes -= currentBytes;

                currIn.read(arrayToCipher, 0, currentBytes);

                System.out.println("DECRYPTION");
                System.out.println(arrayToCipher.length);
                System.out.println(ByteArrayUtils.getHeader(arrayToCipher));

                boolean proceedDecryption = ByteArrayUtils.hasCipheredDenotation(arrayToCipher) &&
                        ByteArrayUtils.getHeader(arrayToCipher).getComplexity().equals(this.complexity.toString());


                int paddingSize = ByteArrayUtils.getHeader(arrayToCipher).getPadding();

                if (proceedDecryption) {

                    while (remainingBytes > 0) {
                        if (remainingBytes >= FileEncryptor.blockSize) {
                            remainingBytes -= FileEncryptor.blockSize;
                            currentBytes = FileEncryptor.blockSize;
                        } else {
                            currentBytes = (int) remainingBytes;
                            remainingBytes = 0;
                        }
                        arrayToCipher = new byte[currentBytes];
                        currIn.read(arrayToCipher, 0, currentBytes);
                        arrayToCipher = CryptoModule.decrypt(this.key, this.complexity, arrayToCipher);
                        if (remainingBytes == 0)
                            arrayToCipher = ByteArrayUtils.removePadding(arrayToCipher, paddingSize);
                        this.fileProvider.saveNextBytes(arrayToCipher);
                    }
                }
            } catch (Exception ex) {
                throw new CryptoException(ex.getMessage(), current.getName());
            }
            currIn.close();
            this.bufferedFile = null;
        }
        else{
            System.out.println("Can use only standard method decryptNext() - use empty constructor to access this");
        }
    }

    private static final int blockSize = 524288;
    private static final int headingBlockSize = 96;
    private FileProvider fileProvider;
    private File bufferedFile;
    private String key;
    private Integer complexity;
    private Header header;
    private boolean allowStandardMethodAccess;
    private boolean allowMultiEncryptions;
    private static final String defaultKey = "INSECURE_PASS";
    private static final Integer defaultComplexity = CryptoModule.REGULAR_MODE;

}
