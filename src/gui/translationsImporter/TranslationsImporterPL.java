package gui.translationsImporter;

import java.util.Map;

/**
 * Class that represents polish version of TranslationImporter - SINGLETON.
 *
 * @since 2018-17-01
 */
public class TranslationsImporterPL extends TranslationsImporter {

    /**
     * Performs translations.
     *
     * @return true if translation went ok, false otherwise.
     */
    public Boolean translate() {
        String translationPath = "PL.json";

        m_translationsMap = super.parseTranslations(translationPath);
        return m_translationsMap != null;
    }

    /**
     * Gets translations.
     *
     * @return An Map<String, String> that has translation mappings.                                                                                                                                                                                                                                                            ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String> with required string as key and its translation as value.
     */
    public Map<String, String> getTranslations() {
        return m_translationsMap;
    }

    /**
     * Static function that gets instance of TranslationImporter.
     * Creates new if does not exists.
     *
     * @return instance of polish version of TranslationsImporter.
     */
    static TranslationsImporterPL getInstance() {
        if (instance == null) {
            instance = new TranslationsImporterPL();
        }

        return instance;
    }

    private TranslationsImporterPL() {
    }

    private static TranslationsImporterPL instance = null;
    private Map<String, String> m_translationsMap;
}
