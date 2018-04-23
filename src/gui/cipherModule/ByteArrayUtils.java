package gui.cipherModule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class allowing easy header managemen on a byte array
 *
 * @author Paweł Talaga
 * @version 1.1
 * @since 2018-13-03
 *
 */
public class ByteArrayUtils {

    private static final Integer maxMessageLength = 99;
    private static final String cipheredDenotation = "2654435769/1640531527";

    /**
     * Adds header to specified byte array
     *
     * @param header Header instance being the header to add
     * @param array byte array the header is being attached to
     *
     * @return byte array with attached header
     * @throws UnsupportedEncodingException when there's unsupported encoding in headers message
     */
    public static byte[] addHeader(Header header, byte[] array)
            throws UnsupportedEncodingException{

        String helpMessage = header.getHelpMessage();
        String padding = header.getRawPadding();
        String complexity = header.getComplexity();

        if(helpMessage.length() > maxMessageLength){
            //IT SHOULD GO WRONG
        }

        Integer length = helpMessage.getBytes("UTF-8").length;

        String lengthStr = Integer.toString(length);
        if(length < 10)
            lengthStr = "0"+lengthStr;

        Byte[] headerLengthAsBytes = toObjects(lengthStr.getBytes());
        Byte[] paddingAsBytes = toObjects(padding.getBytes());
        Byte[] complexityAsBytes = toObjects(complexity.getBytes());
        Byte[] denotationAsBytes = toObjects(ByteArrayUtils.cipheredDenotation.getBytes());
        Byte[] headerAsBytes = toObjects(helpMessage.getBytes("UTF-8"));

        List<Byte> combinedList = new ArrayList<>(Arrays.asList(denotationAsBytes));

        combinedList.addAll(Arrays.asList(paddingAsBytes));
        combinedList.addAll(Arrays.asList(complexityAsBytes));
        combinedList.addAll(Arrays.asList(headerLengthAsBytes));
        combinedList.addAll(Arrays.asList(headerAsBytes));
        combinedList.addAll(Arrays.asList((toObjects(array))));

        Byte[] combined = combinedList.toArray(new Byte[combinedList.size()]);
        byte[] output = new byte[combined.length];

        for(int i = 0; i < combined.length; i++)
            output[i] = combined[i];

        return output;
    }

    /**
     * Returns the header of a byte array, checks the header length
     * which is contained in the first two bytes
     *
     * @param array array to get the header from
     *
     * @return Header instance being the header of the byte array given
     * @throws UnsupportedEncodingException when there's unsupported encoding in headers message
     * @throws CryptoException whenever array hasn't got encryption denotation
     */
    public static Header getHeader(byte[] array)
            throws UnsupportedEncodingException, CryptoException{

        byte[] denotation = ByteArrayUtils.cipheredDenotation.getBytes();

        if(!ByteArrayUtils.hasCipheredDenotation(array))
            throw new CryptoException("Plik nie jest zaszyfrowany / nie ma nagłówka");

        char[] paddingAsChars = {(char)array[denotation.length],
                (char)array[1+denotation.length],
                (char)array[2+denotation.length],
                (char)array[3+denotation.length],
                (char)array[4+denotation.length],
                (char)array[5+denotation.length]};
        char[] algorithmAsChars = {(char)array[6+denotation.length]};
        char[] lengthAsChars = {(char)array[7+denotation.length],
                (char)array[8+denotation.length]};

        Integer headerSize = new Integer(new String(lengthAsChars));

        byte[] message = new byte[headerSize];
        System.arraycopy(array,6 + denotation.length, message, 0, headerSize);

        String messageStr = new String(message, "UTF-8");
        String paddingStr = new String(paddingAsChars);
        String algorithmStr = new String(algorithmAsChars);

        return new Header(messageStr, algorithmStr, paddingStr);
    }

    /**
     * Removes the header from a byte array, checks the header length
     * which is contained in the first two bytes and removes correct
     * number of bytes from array.
     *
     * @param array byte array from which the header is going to be removed
     * @return byte array without header
     * @throws CryptoException whenever array hasn't got encryption denotation
     */
    public static byte[] removeHeader(byte[] array) throws CryptoException{

        byte[] denotation = ByteArrayUtils.cipheredDenotation.getBytes();

        if(!ByteArrayUtils.hasCipheredDenotation(array))
            throw new CryptoException("Plik nie jest zaszyfrowany / nie ma nagłówka");

        char[] lengthAsChars = {(char)array[7+denotation.length],
                (char)array[8+denotation.length]};
        Integer headerSize = new Integer(new String(lengthAsChars));

        byte[] cleaned = new byte[array.length - denotation.length - headerSize - 6];

        System.arraycopy(array, headerSize + 6 + denotation.length, cleaned,
                0,array.length - denotation.length - headerSize - 6);

        return cleaned;
    }

    /**
     * Determines if byte array has header of this version attached
     *
     * @param array byte array to check
     * @return boolean value determining whether array has header of this version added
     */
    public static boolean hasCipheredDenotation(byte[] array){

        byte[] denotation = ByteArrayUtils.cipheredDenotation.getBytes();
        byte[] denotationInArray = new byte[denotation.length];

        System.arraycopy(array, 0, denotationInArray, 0, denotation.length);

        return ByteArrayUtils.cipheredDenotation.equals(new String(denotationInArray));

    }

    /**
     * Returns the length of a header bytes in byte array if it was attached to one
     *
     * @param header header to check
     *
     * @return integer value being the length of a header
     * @throws CryptoException whenever there is unsupported encoding in header's message
     */
    public static int getHeaderBytesSize(Header header) throws CryptoException{
        byte[] arr = new byte[0];
        try {
            arr = addHeader(header, arr);
        }
        catch(UnsupportedEncodingException ex){
            throw new CryptoException("HeaderEncodingERR");
        }
        return arr.length;
    }

    /**
     * Creates header as byte array with padding
     *
     * @param header the Header object to use
     * @param headingBlockSize outgoing block size
     *
     * @return byte array being header as bytes with padding attached
     * @throws CryptoException whenever there is unsupported encoding in header's message
     * @throws UnsupportedEncodingException whenever there is unsupported encoding in header's message
     */
    public static byte[] createHeadingBlock(Header header, int headingBlockSize)
            throws CryptoException, UnsupportedEncodingException{
        int headerSize = ByteArrayUtils.getHeaderBytesSize(header);
        byte[] paddingBlock = new byte[headingBlockSize - headerSize]        ;
        for(int i = 0; i < paddingBlock.length; i++){
            paddingBlock[i] = 0;
        }
        return ByteArrayUtils.addHeader(header, paddingBlock);
    }


    /**
     * Returns padding size required to denote in header for specified size of
     * cipher block and specific file length
     *
     * @param blockSize size of single ciphering block [bytes]
     * @param totalFileLength size of the file [bytes]
     *
     * @return integer being required padding to attach
     */
    public static int getPaddingSize(int blockSize, long totalFileLength){
        while(totalFileLength - blockSize > 0){
            totalFileLength -= blockSize;
        }
        return blockSize - (int)totalFileLength;
    }

    /**
     * Extends an byte array to specified size with zeroes
     *
     * @param array byte array to extend
     * @param blockSize required size of output
     *
     * @return byte array extended to required size
     * @throws IOException whenever there is memory access violation during stream-based array concatenation
     */
    public static byte[] fillArrayToBlockSize(byte[] array, int blockSize)
    throws IOException{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(array);

        byte[] padding = new byte[blockSize - array.length];
        for(int i = 0; i < padding.length; i++){
            padding[i] = 0;
        }

        outputStream.write(padding);

        outputStream.close();

        return outputStream.toByteArray();
    }

    /**
     * Removes padding from byte array
     *
     * @param array byte array with padding to be removed
     * @param paddingSize padding size to be removed
     *
     * @return byte array with padding removed
     */
    public static byte[] removePadding(byte[] array, int paddingSize){
        byte[] cleanArr = new byte[array.length - paddingSize];
        System.arraycopy(array, 0, cleanArr, 0, cleanArr.length);
        return cleanArr;
    }


    /**
     *  Converts byte array to Byte array
     * @param bytesPrim byte array to convert
     * @return Byte array conversion of input
     */
    private static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }
}
