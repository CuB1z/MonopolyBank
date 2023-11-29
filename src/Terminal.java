import java.util.Scanner;

public class Terminal {
    private TranslatorManager translatorManager;
    
    // Constructor ========================================================================================================
    public Terminal() throws Exception {
        this.translatorManager = new TranslatorManager();
    }

    // Public methods =====================================================================================================

    // If needed, override this method in a subclass
    public int readInt(){
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();
        scanner.close();
        
        return answer;
    }
    
    // If needed, override this method in a subclass
    public String readStr(){
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        scanner.close();

        return answer;
    }

    // If needed, override this method in a subclass
    public void show(String message) {
        Translator translator = this.translatorManager.getTranslator();
        String translatedMessage = translator.translate(message);

        System.out.println(translatedMessage);
    }

    // Getters and setters
    public TranslatorManager getTranslatorManager() {
        return this.translatorManager;
    }
}