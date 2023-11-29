import java.util.Scanner;

public class Terminal {
    private TranslatorManager translatorManager;
    private Scanner scanner;
    
    // Constructor ========================================================================================================
    public Terminal() throws Exception {
        this.translatorManager = new TranslatorManager();
        this.scanner = new Scanner(System.in);
    }

    // Public methods =====================================================================================================
    // If needed, you can override these methods in a subclass in order to use a different input/output method
    
    public int readInt(){
        int answer = scanner.nextInt();        
        return answer;
    }
    
    public String readStr(){
        String answer = this.scanner.next();
        return answer;
    }

    public void finalize() {
        this.scanner.close();
    }

    public void show(String message) {
        Translator translator = this.translatorManager.getTranslator();
        String translatedMessage = translator.translate(message);

        System.out.println(translatedMessage);
    }

    // Getters and setters =================================================================================================
    public TranslatorManager getTranslatorManager() {
        return this.translatorManager;
    }
}