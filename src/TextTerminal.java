import java.util.Scanner;

public class TextTerminal extends Terminal {
    private Scanner scanner;
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public TextTerminal() throws Exception {
        super();
        this.scanner = new Scanner(System.in);
    }

    // Public methods =====================================================================================================

    // Method to read an integer
    public int readInt(){
        while (true) {

            // Try to read an integer
            try {
                System.out.print(">> ");
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
    public String readStr() {
        System.out.print(">> ");
        String answer = this.scanner.next();
        return answer;
    }

    // Method to show a translated message
    public void show(String message) {
        Translator translator = this.translatorManager.getTranslator();
        String translatedMessage = translator.translate(message);

        System.out.println(translatedMessage);
    }

    // Method to flush the screen
    public void flushScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Method to wait for the user to press enter
    public void waitForEnter() {
        this.show("Introduce cualquier valor para continuar...");
        this.scanner.next();
    }
}
