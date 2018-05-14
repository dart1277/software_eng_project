package gui.translationsImporter;

import com.owlike.genson.Genson;

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
     * @return A Map  that has translation mappings.                                                                                                                                                                                                                                                            ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String> with required string as key and its translation as value.
     */
    public abstract Map<String, String> getTranslations();

    /**
     * Abstract function to perform translations.
     *
     * @return true if translation went ok, false otherwise.
     */
    public abstract Boolean translate();

    /**
     * Function that parses translations.
     *
     * @param translationFilePath path to translation file.
     * @return true if translation went ok, false otherwise.
     */
    @SuppressWarnings("unchecked")
    Map<String, String> parseTranslations(String translationFilePath) {
        String entireJson = "{}";
        try (Scanner scn = new Scanner(getClass().getResourceAsStream(translationFilePath))) {
            final String pattern = "\\A";
            scn.useDelimiter(pattern);
            if (scn.hasNext())
                entireJson = scn.useDelimiter(pattern).next();
        }

        Genson genson = new Genson();
        return genson.deserialize(entireJson, Map.class);
    }

}
