package gui.translationsImporter;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
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
    public void getInstance() {

        TranslationsImporter instanceEng2 = TranslationsImporterENG.getInstance();
        TranslationsImporter instancePl2 = TranslationsImporterPL.getInstance();

        assertNotNull(instanceEng1);
        assertNotNull(instancePl1);

        assertTrue(instanceEng1 == instanceEng2);
        assertTrue(instancePl1 == instancePl2);
    }

    @Test
    public void translate(){

        boolean engMap = instanceEng1.translate();
        boolean plMap = instancePl1.translate();

        if(engMap && plMap)
        {
                assertNotEquals(instanceEng1.getTranslations(), instancePl1.getTranslations());
        }
        else
            {
               if(engMap) assertNull(instanceEng1.getTranslations());
               if(plMap) assertNull(instancePl1.getTranslations());
            }
    }

    @Test
    public void getPathSuffix() {
/*
        String pathToFile = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            pathToFile = "\\src\\gui\\translationsImporter\\SomeFile.txt";
        }
        else {
            pathToFile = "/src/gui/translationsImporter/SomeFile.txt";
        }

        assertNotNull(instanceEng1.getPathSuffix("SomeFile.txt"));
        assertNotNull(instancePl1.getPathSuffix("SomeFile.txt"));
        assertEquals(pathToFile, instanceEng1.getPathSuffix("SomeFile.txt"));
        assertEquals(pathToFile, instancePl1.getPathSuffix("SomeFile.txt"));
        */
        assertEquals("SomeName", instanceEng1.getPathSuffix("SomeName"));
        assertEquals("SomeNamePL", instancePl1.getPathSuffix("SomeNamePL"));
    }

    @Test
    public void parseTranslations(){
        Map<String, String> testMap = new HashMap<>();
        testMap.put("firstKey","firstValue");
        testMap.put("secondKey","secondValue");
        testMap.put("thirdKey","thirdValue");

        String testJsonFile = "TestJSON.json";
        /*
        String basePath = new File("").getAbsolutePath();
        String pathToFile = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            pathToFile = "\\test\\gui\\translationsImporter\\TestJSON.json";
        }
        else {
            pathToFile = "/test/gui/translationsImporter/TestJSON.json";
        }
        */
        assertEquals(testMap, instanceEng1.parseTranslations(testJsonFile));
        assertEquals(testMap, instancePl1.parseTranslations(testJsonFile));

        try {
            assertEquals("{}", instanceEng1.parseTranslations("NotExistig.json"));
            fail("Exception was not thrown!");
        }
        catch (NullPointerException e)
        {
            System.out.println(e.getCause());
        }
    }


}