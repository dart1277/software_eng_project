package gui.translationsImporter;

import com.owlike.genson.Genson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

/**
 * Class that represents polish TranslationImporter - SINGLETON.
 *
 * @since 2018-17-01
 */
public class TranslationsImporterPL extends TranslationsImporter {
    private Map<String, String> m_translationsMap;

    private TranslationsImporterPL() {
    }

    private static TranslationsImporterPL instance = null;

    public static TranslationsImporterPL getInstance() {
        if (instance == null) {
            instance = new TranslationsImporterPL();
        }

        return instance;
    }

    public Boolean translate() {
        String basePath = new File("").getAbsolutePath();
        String translationPath = basePath + getPathSuffix("PL.json");

        m_translationsMap = super.parseTranslations(translationPath);
        return m_translationsMap != null;
    }

    public Map<String, String> getTranslations() {
        return m_translationsMap;
    }
}
