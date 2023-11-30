import java.util.ArrayList;

import utils.Constants;

public class TranslatorManager {
    private Translator currentLanguage;
    private ArrayList<Translator> languages = new ArrayList<Translator>();

    // Constructor ========================================================================================================
    public TranslatorManager() throws Exception {
        for (String lang : Constants.AVAILABLE_LANGUAGES) {
            this.addLanguage(lang);
        }

        this.setLanguage(Constants.DEFAULT_LANGUAGE);
    }

    // Public methods =====================================================================================================
    public void addLanguage(String fileName) throws Exception {
        fileName = String.format(Constants.LANGUAGES_FILE_PATH, fileName);
        Translator translator = new Translator(fileName);

        this.languages.add(translator);
    }

    // Getters and setters ================================================================================================
    public Translator getTranslator() {
        return this.currentLanguage;
    }

    public void setLanguage(String language) {
        for (Translator translator : languages) {
            if (translator.getLanguage().equals(language)) {
                this.currentLanguage = translator;
            }
        }
    }
}
