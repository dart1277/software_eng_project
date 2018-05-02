package gui.cipherModule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderTest {

    private static Header _header;

    @BeforeClass
    public static void setUp() throws Exception {
        _header = new Header("HelpMess","1","000000");
    }

    @Test
    public void setComplexityException(){
        try{
            new Header("help","22","000000");
            fail("Exception was not thrown");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Complexity string must contain single character",e.getMessage());
        }
    }

    @Test
    public void setPaddingException(){
        try{
            new Header("help","2","0");
            fail("Exception was not thrown");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Padding string must contain exactly six characters",e.getMessage());
        }
    }

    @Test
    public void setHelpMessage() {
        assertEquals("HelpMess",_header.getHelpMessage());

        _header.setHelpMessage("NewHelp");
        assertEquals("NewHelp",_header.getHelpMessage());
    }

    @Test
    public void setComplexity() {
        assertEquals("1",_header.getComplexity());

        _header.setComplexity(2);
        assertEquals("2",_header.getComplexity());
    }

    @Test
    public void setPadding() {
        assertEquals("000000",_header.getRawPadding());
        assertEquals(new Integer("000000"), _header.getPadding());

        _header.setPadding(9);
        assertEquals("000009", _header.getRawPadding());
        assertEquals(new Integer("000009"),_header.getPadding());
    }

}