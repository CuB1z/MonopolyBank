package src;

import java.io.Serializable;

public abstract class Terminal implements Serializable {
    protected TranslatorManager translatorManager;
    
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public Terminal() throws Exception {
        this.translatorManager = new TranslatorManager();
    }

    // Public methods ===================================================================================================== 
    public abstract int readInt();
    public abstract String readStr();
    public abstract void show(String message);

    // Methods to flush the screen
    public abstract void flushScreen();
    public abstract void waitForEnter();

    // Getters & setters ==================================================================================================
    public TranslatorManager getTranslatorManager() {
        return this.translatorManager;
    }

    public void setTranslatorManager(TranslatorManager translatorManager) {
        this.translatorManager = translatorManager;
    }
}