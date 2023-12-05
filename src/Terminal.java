import java.io.Serializable;
import java.util.Scanner;

public abstract class Terminal implements Serializable {
    protected TranslatorManager translatorManager;
    protected Scanner scanner;
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public Terminal() throws Exception {
        this.translatorManager = new TranslatorManager();
        this.scanner = new Scanner(System.in);
    }

    // Public methods ===================================================================================================== 
    public abstract int readInt();
    public abstract String readStr();
    public abstract void show(String message);

    // Getters & setters ==================================================================================================
    public TranslatorManager getTranslatorManager() {
        return this.translatorManager;
    }

    public void setTranslatorManager(TranslatorManager translatorManager) {
        this.translatorManager = translatorManager;
    }
}