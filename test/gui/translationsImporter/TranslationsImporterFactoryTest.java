package gui.translationsImporter;

import static org.junit.Assert.*;

public class TranslationsImporterFactoryTest {

    @org.junit.Test
    public void getTranslationImporter(){

        TranslationsImporter pl = TranslationsImporterPL.getInstance();
        TranslationsImporter en = TranslationsImporterENG.getInstance();

        assertEquals(pl, TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterPL));
        assertEquals(en, TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG));

    }
}