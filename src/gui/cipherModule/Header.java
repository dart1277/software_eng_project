package gui.cipherModule;

/**
 * Class representing a header of encrypted file
 *
 * @author Paweł Talaga
 * @version 1.1
 * @since 2018-13-03
 */
public class Header {

    /**
     * Constructor allowing to creation of a header instance
     *
     * @param helpMessage message to help the client remember password
     * @param complexity  complexity of the ciphering process used
     * @param padding     padding size added after the file
     */
    Header(String helpMessage, String complexity, String padding) {
        this.helpMessage = helpMessage;
        if (complexity.length() != 1) {
            throw new IllegalArgumentException("Complexity string must contain single character");
        }
        this.complexity = complexity;
        if (padding.length() != 6) {
            throw new IllegalArgumentException("Padding string must contain exactly six characters");
        }
        this.padding = padding;
    }


    /**
     * Sets hinting message value in the header
     *
     * @param helpMessage String being the message to attach
     */
    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }


    /**
     * Sets String complexity field in header based on integer complexity given
     * Complexity should be an Integer from 1 to 3
     *
     * @param complexity Integer in the range from 1 to 3
     */
    public void setComplexity(Integer complexity) {
        this.complexity = complexity.toString();
    }


    /**
     * Simple getter for complexity
     *
     * @return string denoting ciphering complexity
     */
    public String getComplexity() {
        return complexity;
    }


    /**
     * Simple getter for helpMessage
     *
     * @return string being the message client has set up to remind password
     */
    public String getHelpMessage() {
        return helpMessage;
    }


    /**
     * Simple getter for version
     *
     * @return string containing information about program version
     */
    public Integer getPadding() {
        return new Integer(padding);
    }


    /**
     * Returns padding field as it is
     *
     * @return String padding field value
     */
    public String getRawPadding() {
        return padding;
    }


    /**
     * Sets padding String value depending on int input
     *
     * @param padding length of padding block
     */
    public void setPadding(int padding) {
        String paddPrim = Integer.toString(padding);
        if (padding < 10)
            paddPrim = "00000" + paddPrim;
        else if (padding < 100)
            paddPrim = "0000" + paddPrim;
        else if (padding < 1000)
            paddPrim = "000" + paddPrim;
        else if (padding < 10000)
            paddPrim = "00" + paddPrim;
        else if (padding < 100000)
            paddPrim = "0" + paddPrim;
        this.padding = paddPrim;
    }


    @Override
    public String toString() {
        return "Complexity: " + getComplexity() + "\nPadding: " + getPadding() + "\nMSG: " + getHelpMessage();
    }


    private String helpMessage;
    private String complexity;
    private String padding;

}
