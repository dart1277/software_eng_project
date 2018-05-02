package gui.cipherModule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class HeaderTest {

    private  Header _header;
    private  static String expectedPadd;
    private  static Integer inputPadd;
    private  static String expectedComp;
    private  static Integer inputComp;

    @Before
    public void setUp() throws Exception {
        _header = new Header("HelpMess","1","000000");
    }


    public HeaderTest(Integer inputPadd, String expectedPadd, Integer inputComp, String expectedComp){
        this.inputPadd = inputPadd;
        this.expectedPadd = expectedPadd;
        this.inputComp = inputComp;
        this.expectedComp = expectedComp;
    }

    @Parameters
    public static Collection<Object[]> paddings(){
        return Arrays.asList(new Object[][] {{9,"000009", 1, "1"}, {59,"000059", 2, "2"}, {529, "000529", 3, "3"}, {6599, "006599", 4, "4"}, {60500, "060500", 5, "5"}});

    }

    @Test
    public void setComplexityException(){
        try{
            new Header("help",expectedComp + "0","000000");
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
            new Header("help","2",inputPadd.toString());
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

        _header.setComplexity(inputComp);
        assertEquals(expectedComp,_header.getComplexity());
    }



    @Test
    public void setPadding() {
        assertEquals("000000",_header.getRawPadding());
        assertEquals(new Integer("000000"), _header.getPadding());

        _header.setPadding(inputPadd);
        assertEquals(expectedPadd, _header.getRawPadding());
        assertEquals(new Integer(expectedPadd),_header.getPadding());
    }

    @Test
    public void toStingTest(){

        _header.setComplexity(inputComp);
        _header.setPadding(inputPadd);
        String testString = "Complexity: " + _header.getComplexity() + "\nPadding: " + _header.getPadding() + "\nMSG: " + _header.getHelpMessage();
        assertEquals(testString, _header.toString());
    }

}