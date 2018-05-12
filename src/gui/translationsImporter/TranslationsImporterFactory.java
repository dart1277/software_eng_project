package gui.translationsImporter;

/**
 * Factory class to get TranslationImporter
 *
 * @since 2018-17-04
 */
public class TranslationsImporterFactory {

    /**
     * Gets instance of TranslationImporter.
     *
     * @param translationImporterType String that tells about which TranslationImporter we want to get.
     * @return instance of desired type of TranslationsImporter
     */
    public static TranslationsImporter getTranslationImporter(TranslationsImporterType translationImporterType) {

        switch (translationImporterType) {
            case TranslationsImporterPL:
                return TranslationsImporterPL.getInstance();
            case TranslationsImporterENG:
                return TranslationsImporterENG.getInstance();
        }

        return null;
    }
}
