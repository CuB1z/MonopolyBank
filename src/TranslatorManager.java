package src;

import java.io.Serializable;
import java.util.*;
import utils.*;

public class TranslatorManager implements Serializable {
    private Translator currentLanguage;
    private List<Translator> languages = new ArrayList<Translator>();

    // Constructor ========================================================================================================

    // Zero Constructor
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

    public void setLanguage(String language) {
            
        for (Translator translator : languages) {
            if (translator.getLanguage().equals(language))
                this.currentLanguage = translator;
        }
    }

    // Getters & setters ==================================================================================================
    public Translator getTranslator() {
        return this.currentLanguage;
    }

    public void setTranslator(Translator translator) {
        this.currentLanguage = translator;
    }


    public List<Translator> getLanguages() {
        return this.languages;
    }

    public void setLanguages(List<Translator> languages) {
        this.languages = languages;
    }
}
