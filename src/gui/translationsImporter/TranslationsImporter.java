package gui.translationsImporter;

import com.owlike.genson.Genson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * Abstract class that represents TranslationsImporter.
 *
 * @since 2018-17-01
 */
public abstract class TranslationsImporter {

    /**
     * Abstract function to get translations.
     *
     * @return Map<String                                                                                                                               ,                                                                                                                                                                                                                                                               String> with required string as key and its translation as value.
     */
    abstract public Map<String, String> getTranslations();

    abstract public Boolean translate();

    public String getPathSuffix(String configFileName) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return configFileName;
        } else {
            return configFileName;
        }
    }

    @SuppressWarnings("unchecked")
    Map<String, String> parseTranslations(String translationPath) {
        String entireJson = "{}";
        Scanner scn = new Scanner(getClass().getResourceAsStream(translationPath));
        entireJson = scn.useDelimiter("\\A").next();
        scn.close();
        Genson genson = new Genson();
        return genson.deserialize(entireJson, Map.class);
    }

}
