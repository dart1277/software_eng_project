package gui.translationsImporter;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TranslationsImporterTest {

    private  static TranslationsImporter instanceEng1;
    private  static TranslationsImporter instancePl1;

    @BeforeClass
    public static void setUp(){
        instanceEng1 = TranslationsImporterENG.getInstance();
        instancePl1 =  TranslationsImporterPL.getInstance();
    }

    @Test
    public void getTranslations() {

        TranslationsImporter instanceEng2 = TranslationsImporterENG.getInstance();
        TranslationsImporter instancePl2 = TranslationsImporterPL.getInstance();

        assertNotNull(instanceEng1);
        assertNotNull(instancePl1);
        assertTrue(instanceEng1 == instanceEng2);
        assertTrue(instancePl1 == instancePl2);

    }

    @Test
    public void translate(){

        if(instanceEng1.translate() && instancePl1.translate())
        {
                assertNotEquals(instanceEng1.getTranslations(), instancePl1.getTranslations());
        }
    }

    @Test
    public void getPathSuffix() {

        String pathToFile = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            pathToFile = "\\src\\gui\\translationsImporter\\SomeFile.txt";
        }
        else {
            pathToFile = "/src/gui/translationsImporter/SomeFile.txt";
        }

        assertEquals(pathToFile, instanceEng1.getPathSuffix("SomeFile.txt"));
    }


}