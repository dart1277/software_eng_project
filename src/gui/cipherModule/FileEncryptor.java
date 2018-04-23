package gui.cipherModule;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;

public class
FileEncryptor {


    public FileEncryptor(ArrayList<String> filePathArray) {
        this.fileProvider = new FileProvider(filePathArray);
    }


    public void configure(String key, Integer complexity, boolean allowMultiEncryptions) {
        this.configure(key, complexity, allowMultiEncryptions, "");
    }


    public void configure(String key, Integer complexity,
                          boolean allowMultiEncryptions, String helpMessage) {
        this.key = key;
        this.complexity = complexity;
        this.header = new Header(helpMessage, complexity.toString(), "000000");
        this.allowMultiEncryptions = allowMultiEncryptions;
    }


    public void encryptAll()
            throws CryptoException, InvalidAlgorithmParameterException {
        while (this.fileProvider.hasNext()) {
            try {
                encryptNext();
            } catch (IOException ex) {
                throw new CryptoException("", ex);
            }
        }
    }

    public void encryptNext()
            throws IOException, CryptoException, InvalidAlgorithmParameterException {
        FileInputStream currIn = null;
        try {
            File current = this.fileProvider.getNext();
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
        } finally {
            if (currIn != null) currIn.close();
            fileProvider.closeOutputFile();
        }
    }


    public void decryptAll()
            throws CryptoException, InvalidAlgorithmParameterException {
        while (this.fileProvider.hasNext()) {
            try {
                decryptNext();
            } catch (IOException ex) {
                System.out.println("IOex");
            }
        }
    }


    public void decryptNext()
            throws IOException, CryptoException, InvalidAlgorithmParameterException {
        File current = this.fileProvider.getNext();
        FileInputStream currIn = new FileInputStream(current);
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
                arrayToCipher = new byte[(int) currentBytes];
                currIn.read(arrayToCipher, 0, (int) currentBytes);
                arrayToCipher = CryptoModule.decrypt(this.key, this.complexity, arrayToCipher);
                if (remainingBytes == 0)
                    arrayToCipher = ByteArrayUtils.removePadding(arrayToCipher, paddingSize);
                this.fileProvider.saveNextBytes(arrayToCipher);
            }
        }
        currIn.close();
    }


    private static final int blockSize = 524288;
    private static final int headingBlockSize = 96;
    private FileProvider fileProvider;
    private String key;
    private Integer complexity;
    private Header header;
    private boolean allowMultiEncryptions;

}
