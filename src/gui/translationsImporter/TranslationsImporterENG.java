package gui.translationsImporter;

import java.util.Map;

/**
 * Class that represents english version of TranslationImporter - SINGLETON.
 *
 * @since 2018-17-01
 */
public class TranslationsImporterENG extends TranslationsImporter {

    /**
     * Performs translations.
     *
     * @return true if translation went ok, false otherwise.
     */
    public Boolean translate() {
        String translationPath = "ENG.json";

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
     * @return instance of english version of TranslationsImporter.
     */
    static TranslationsImporterENG getInstance() {
        if (instance == null) {
            instance = new TranslationsImporterENG();
        }

        return instance;
    }

    private TranslationsImporterENG() {
    }

    private static TranslationsImporterENG instance = null;
    private Map<String, String> m_translationsMap;
}