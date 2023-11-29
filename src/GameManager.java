import java.util.Scanner;

import utils.Constants;

import java.io.File;

public class GameManager {
    private Terminal terminal;

    // Public methods =====================================================================================================
    public void start() throws Exception {

        // Initialize the terminal and the translator
        this.terminal = new TextTerminal();

        // Show the welcome message
        this.terminal.show("BIENVENIDO AL MONOPOLY BANK");
        System.out.println();

        // Ask if the user wants to resume a game
        String fileName = this.askForResumeGame();

        // Create a new game or load a saved one
        Game game = null;
        if (fileName == null) {
            this.terminal.show("Creando nueva partida...");
            game = new Game();
            game.play();

        } else {
            this.terminal.show("Cargando partida...");
            game = new Game(fileName);
        }

        // TODO Check loading game implementation

        // Play the game
        // game.play();
    }

    // Private methods ====================================================================================================
    
    // Ask if the user wants to resume a game
    private String askForResumeGame() {
        this.terminal.show("¿Quieres reanudar una partida? (S/N)");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        System.out.println();
        scanner.close();

        if (answer.equals("S")) return this.showFileNames();
        else return null;

    }

    // Show the file names in the "oldGames" directory
    private String showFileNames() {

        // Get the file names
        File folder = new File(Constants.MONOPOLY_OLD_GAMES_PATH);
        String[] list = folder.list();

        this.terminal.show("Ficheros disponibles: ");

        // Show the file names if there are any
        if (list == null || list.length == 0) {
            this.terminal.show("-- No hay ficheros disponibles");
            this.terminal.show("");
            return null;
        }
        
        for (String name : list) {
            System.out.println(name);
        }

        System.out.println();

        // Ask for the file name
        return this.askForFileName(list);
    }

    // Ask for the file name and check if it exists
    private String askForFileName(String[] fileNames) {
        this.terminal.show("Introduce el nombre del fichero: / (S para salir)");

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        scanner.close();

        boolean fileExists = false;

        do {
            for (String name : fileNames) {
                if (name.equals(fileName))
                    fileExists = true;
            }

            if (!fileExists) {
                this.terminal.show("El fichero no existe, introduce otro nombre: / (S para salir)");
                fileName = scanner.nextLine();
            }
        } while (!fileExists && !fileName.equals("S"));

        if (!fileExists) {
            this.terminal.show("La operacion ha sido cancelada...");
            this.terminal.show("Creando nueva partida...");
            return null;

        } else {
            this.terminal.show("Fichero encontrado...");
            this.terminal.show("Cargando partida...");
            return fileName;
        }
    }
}
