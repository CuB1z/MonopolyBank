package src;

import java.io.Serializable;

import utils.*;

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

        if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
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

        // Show the file names if there are any
        if (list == null || list.length == 0) {
            this.terminal.show("-- No hay ficheros disponibles --");
            this.terminal.show("");
            return null;
        }

        this.terminal.show("Ficheros disponibles: ");

        for (int i = 0; i < list.length; i++) {
            this.terminal.show(i + 1 + ". " + list[i].replace(Constants.DEFAULT_GAMES_EXTENSION, ""));
        }

        this.terminal.show("");

        // Ask for the file name
        String fileName = this.askForFileName(list);
        return fileName;
    }

    // Ask for the file name and check if it exists
    private String askForFileName(String[] fileNames) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("¿Qué juego quieres reanudar? (%d para cancelar)");

        this.terminal.show(String.format(output, Constants.DEFAULT_CANCEL_INT));

        int fileNumber = Constants.DEFAULT_CANCEL_INT - 1;

        // Ask for the file number until it is valid
        while ((fileNumber < 1 || fileNumber > fileNames.length) && fileNumber != Constants.DEFAULT_CANCEL_INT) {
            fileNumber = this.terminal.readInt();

            if ((fileNumber < 1 || fileNumber > fileNames.length) && fileNumber != Constants.DEFAULT_CANCEL_INT) {
                output = trs.translate("El fichero %d no existe, prueba de nuevo: / (%d para salir)");
                this.terminal.show(String.format(output, fileNumber, Constants.DEFAULT_CANCEL_INT));
            }
        }

        if (fileNumber == Constants.DEFAULT_CANCEL_INT) return null;

        this.terminal.show("");

        return fileNames[fileNumber - 1].replace(Constants.DEFAULT_GAMES_EXTENSION, "");
    }
    
    // Getters & setters ==================================================================================================
    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
