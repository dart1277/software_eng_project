package gui.translationsImporter;

import gui.Controller;

/**
 * Factory class to get TranslationImporter
 *
 * @since 2018-17-04
 *
 */
public class TranslationsImporterFactory {

    /**
     * Gets font size.
     * @param translationImporterType String that tells about which TranslationImporter we want to get.
     * @return TranslationsImporter
     */
    public static TranslationsImporter getTranslationImporter(TranslationsImporterType translationImporterType){

        switch(translationImporterType)
        {
            case TranslationsImporterPL:
                return TranslationsImporterPL.getInstance();
            case TranslationsImporterENG:
                return TranslationsImporterENG.getInstance();
        }

        return null;
    }
}
