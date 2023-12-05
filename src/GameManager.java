import java.io.Serializable;
import java.util.Scanner;
import java.io.File;

public class GameManager implements Serializable {

    // Scanner to read the user input before the game starts
    private Scanner scanner;

    // Constructor ========================================================================================================

    // Zero Constructor
    public GameManager() {
        this.scanner = new Scanner(System.in);
    }

    // Public methods =====================================================================================================
    public void start() throws Exception {

        // Show the welcome message
        System.out.println("BIENVENIDO AL MONOPOLY BANK");
        System.out.println();

        // Ask if the user wants to resume a game
        String fileName = this.askForResumeGame();

        // Get absolute path of the file
        if (fileName != null)
            fileName = PathUtils.getFilePath(String.format(Constants.MONOPOLY_OLD_GAME, fileName));

        // Create a new game or load a saved one
        Game game = null;
        if (fileName == null) {
            System.out.println("Creando nueva partida...");
            game = new Game();

        } else {
            System.out.println("Cargando partida...");
            game = FileManager.readFile(fileName);
        }

        // Play the game
        game.play();
    }

    // Private methods ====================================================================================================

    // Ask if the user wants to resume a game
    private String askForResumeGame() {
        System.out.println(String.format("¿Quieres reanudar una partida? (%s/n)", Constants.DEFAULT_APROVE_STRING));
        String answer = this.scanner.next();
        System.out.println();

        if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)){
            String file = this.showFileNames();
            return file;
        }
        else return null;
    }

    // Show the file names in the "oldGames" directory
    private String showFileNames() {

        // Get absolute path
        String path = PathUtils.getFilePath(Constants.MONOPOLY_OLD_GAMES_PATH);

        // Get the file names
        File folder = new File(path);
        String[] list = folder.list();

        System.out.println("Ficheros disponibles: ");

        // Show the file names if there are any
        if (list == null || list.length == 0) {
            System.out.println("-- No hay ficheros disponibles --");
            System.out.println("");
            return null;
        }
        
        for (String name : list) {
            System.out.println(name);
        }

        System.out.println();

        // Ask for the file name
        String fileName = this.askForFileName(list);
        return fileName;
    }

    // Ask for the file name and check if it exists
    private String askForFileName(String[] fileNames) {
        System.out.println(String.format("Introduce el nombre del fichero: / (%s para salir)", Constants.DEFAULT_APROVE_STRING));
        String fileName = this.scanner.next();

        if (fileName.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return null;

        boolean fileExists = false;

        do {
            for (String name : fileNames) {
                if (name.equals(fileName))
                    fileExists = true;
            }

            if (!fileExists) {
                System.out.println(String.format("El fichero %s no existe, prueba de nuevo: / (%s para salir)",
                    fileName, Constants.DEFAULT_APROVE_STRING));

                fileName = this.scanner.next();
            }
        } while (!fileExists && !fileName.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING));

        if (!fileExists) {
            System.out.println("La operacion ha sido cancelada...");
            return null;

        } else {
            System.out.println("Fichero encontrado...");
            return fileName;
        }
    }

    // Getters & setters ==================================================================================================
    public Scanner getScanner() {
        return this.scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
