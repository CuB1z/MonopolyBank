import java.io.Serializable;
import java.io.File;

public class GameManager implements Serializable {

    private Terminal terminal;

    // Constructor ========================================================================================================

    // Zero Constructor
    public GameManager() throws Exception {
        this.terminal = new TextTerminal();
    }

    // Public methods =====================================================================================================
    public void start() throws Exception {

        // Show the welcome message
        this.terminal.show("BIENVENIDO AL MONOPOLY BANK");
        this.terminal.show("");

        // Ask if the user wants to resume a game
        String fileName = this.askForResumeGame();
        this.terminal.flushScreen();
        
        // Create a new game or load a saved one
        Game game = null;
        if (fileName == null) {
            this.terminal.show("Creando nueva partida...");
            game = new Game();

        } else {
            this.terminal.show("Cargando partida...");
            game = FileManager.readFile(fileName);
        }

        // Play the game
        game.play();
    }

    // Private methods ====================================================================================================

    // Ask if the user wants to resume a game
    private String askForResumeGame() {
        this.terminal.show(String.format("¿Quieres reanudar una partida? (%s/n)", Constants.DEFAULT_APROVE_STRING));
        String answer = this.terminal.readStr();
        this.terminal.show("");

        if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)){
            String file = this.showFileNames();
            this.terminal.waitForEnter();
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

        this.terminal.show("Ficheros disponibles: ");

        // Show the file names if there are any
        if (list == null || list.length == 0) {
            this.terminal.show("-- No hay ficheros disponibles --");
            this.terminal.show("");
            return null;
        }
        
        for (String name : list) {
            this.terminal.show(name);
        }

        this.terminal.show("");

        // Ask for the file name
        String fileName = this.askForFileName(list);
        return fileName;
    }

    // Ask for the file name and check if it exists
    private String askForFileName(String[] fileNames) {
        this.terminal.show(String.format("Introduce el nombre del fichero: / (%s para salir)", Constants.DEFAULT_APROVE_STRING));
        String fileName = this.terminal.readStr();

        if (fileName.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return null;

        boolean fileExists = false;

        do {
            for (String name : fileNames) {
                if (name.replace(Constants.DEFAULT_GAMES_EXTENSION, "").equals(fileName))
                    fileExists = true;
            }

            if (!fileExists) {
                this.terminal.show(String.format("El fichero %s no existe, prueba de nuevo: / (%s para salir)",
                    fileName, Constants.DEFAULT_APROVE_STRING));

                fileName = this.terminal.readStr();
            }
        } while (!fileExists && !fileName.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING));

        this.terminal.show("");

        if (!fileExists) {
            this.terminal.show("La operacion ha sido cancelada...");
            return null;

        } else {
            this.terminal.show("Fichero encontrado...");
            return fileName;
        }
    }
    
    // Getters & setters ==================================================================================================
    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
