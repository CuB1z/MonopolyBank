package src;

import java.util.Scanner;

public class TextTerminal extends Terminal {
    private Scanner scanner;
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public TextTerminal() {
        super();
        this.scanner = new Scanner(System.in);
    }

    // Public methods =====================================================================================================

    // Method to read an integer
    public int readInt(){
        while (true) {

            // Try to read an integer and return it if it is not empty
            try {
                System.out.print(">> ");
                String answer = this.scanner.nextLine();

                if (answer.trim().length() == 0) {
                    this.show("Enter a valid number");
                    this.show("");
                    continue;
                }

                int value = Integer.parseInt(answer);
                return value;

            // If the input is not an integer, show an error message
            } catch (Exception e) {
                this.show("Enter a valid number");
                this.show("");
            }
        }
    }

    // Method to read a string
    public String readStr() {
        while (true) {
            // Try to read a string and return it if it is not empty
            System.out.print(">> ");
            String answer = this.scanner.nextLine();
            if (answer.trim().length() > 0) return answer;

            // If the input is empty, show an error message
            this.show("Enter a valid input");
            this.show("");
        }
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
        String output = this.getTranslatorManager().getTranslator().translate("Press <Enter> to continue");
        System.out.print(output + "\t");
        this.scanner.nextLine();
    }
}