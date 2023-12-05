public class TextTerminal extends Terminal {
    
    // Constructor ========================================================================================================

    // Zero Constructor
    public TextTerminal() throws Exception {
        super();
    }

    // Public methods =====================================================================================================

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
    public String readStr() {
        String answer = this.scanner.next();
        return answer;
    }

    // Method to show a translated message
    public void show(String message) {
        Translator translator = this.translatorManager.getTranslator();
        String translatedMessage = translator.translate(message);

        System.out.println(translatedMessage);
    }
}
