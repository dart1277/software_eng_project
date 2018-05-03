package gui.translationsImporter;

import com.owlike.genson.Genson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

/**
 * Class that represents english TranslationImporter - SINGLETON.
 *
 * @since 2018-17-01
 *
 */
public class TranslationsImporterENG extends TranslationsImporter {
    private Map<String, String> m_translationsMap;

    private TranslationsImporterENG(){
    }

    private static TranslationsImporterENG instance = null;

    public static TranslationsImporterENG getInstance(){
        if(instance == null){
            instance = new TranslationsImporterENG();
        }

        return instance;
    }

    public Boolean translate(){
        String translationPath = getPathSuffix("ENG.json");

        m_translationsMap = super.parseTranslations(translationPath);
        return m_translationsMap != null;
    }

    public Map<String, String> getTranslations(){ return m_translationsMap; }
}