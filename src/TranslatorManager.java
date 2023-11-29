import java.util.ArrayList;

import utils.Constants;

public class TranslatorManager {
    private Translator currentLanguage;
    private ArrayList<Translator> languages = new ArrayList<Translator>();

    // Constructor ========================================================================================================
    public TranslatorManager() throws Exception {
        String fileName = String.format(Constants.LANGUAGES_FILE_PATH, Constants.DEFAULT_LANGUAGE);
        this.currentLanguage = new Translator(fileName);

        this.languages.add(this.currentLanguage);
    }

    public TranslatorManager(String fileName) throws Exception {
        fileName = String.format(Constants.LANGUAGES_FILE_PATH, fileName);
        this.currentLanguage = new Translator(fileName);

        this.languages.add(this.currentLanguage);
    }

    // Public methods =====================================================================================================
    public void changeLanguage() {

    }

    // Getters and setters =================================================================================================
    public Translator getTranslator() {
        return this.currentLanguage;
    }
}
