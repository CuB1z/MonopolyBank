import java.util.Scanner;
import java.io.Serializable;

public class Terminal implements Serializable{
    private TranslatorManager translatorManager;
    private Scanner scanner;
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public Terminal() throws Exception {
        this.translatorManager = new TranslatorManager();
        this.scanner = new Scanner(System.in);
    }

    // Public methods =====================================================================================================
    // If needed, you can override these methods in a subclass in order to use a different input/output method
    
    // Method to read an integer
    public int readInt(){
        while (true) {

            // Try to read an integer
            try {
                String answer = this.scanner.next();  
                int value = Integer.parseInt(answer);
                return value;

            // If the input is not an integer, show an error message
            } catch (Exception e) {
                this.show("Introduce un numero valido");
            }  
        }
    }
    
    // Method to read a string
    public String readStr(){
        String answer = this.scanner.next();
        return answer;
    }

    // Method to close the scanner
    public void finalize() {
        this.scanner.close();
    }

    // Method to show a translated message
    public void show(String message) {
        Translator translator = this.translatorManager.getTranslator();
        String translatedMessage = translator.translate(message);

        System.out.println(translatedMessage);
    }

    // Getters & setters ==================================================================================================
    public TranslatorManager getTranslatorManager() {
        return this.translatorManager;
    }

    public void setTranslatorManager(TranslatorManager translatorManager) {
        this.translatorManager = translatorManager;
    }
}