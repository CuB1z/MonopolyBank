import java.io.*;

import utils.Constants;
import utils.PathUtils;

public class Game implements Serializable{
    private MonopolyCode [] monopolyCodeArray = new MonopolyCode[80];
    private Terminal terminal;
    private Player [] players = new Player[4];

    // Constructor ========================================================================================================
    public Game() throws Exception {
        this.terminal = new TextTerminal();
        this.createPlayers();
        this.loadMonopolyCodes();
    }

    public Game(String fileName) throws Exception {
        this.loadMonopolyCodes();
    }

    // Public methods =====================================================================================================
    public void play() {
        System.out.println("Jugando...");
    }

    // Private methods ====================================================================================================
    private void createPlayers(){
        
        // Ask for the number of players
        this.terminal.show("Introduce el numero de jugadores (2-4): ");
        int answer = this.terminal.readInt();
        this.terminal.show("");

        if (answer < 2 || answer > Constants.MAX_NUMBER_OF_PLAYERS) {
            this.terminal.show("Numero de jugadores incorrecto");
            this.createPlayers();
        } else {
            // Translate the message
            Translator trs = this.terminal.getTranslatorManager().getTranslator();
            String msg = trs.translate("Introduce el nombre del jugador ");

            // Create the players
            for (int i = 0; i < answer; i++) {
                this.terminal.show(msg + (i + 1) + ": ");
                String name = this.terminal.readStr();
                this.terminal.show("");

                this.players[i] = new Player(i, name, this.terminal);
                System.out.println(this.players[i].toString());
            }
        }
    }

    private void loadMonopolyCodes() throws Exception {
        String file = PathUtils.getFilePath(Constants.MONOPOLY_CODE_FILE_PATH);

        Reader in = new FileReader(file);
        BufferedReader buffer = new BufferedReader(in);

        String line;

        do {
            line = buffer.readLine();
            
            if (line != null) {
                
            }
        } while (line != null);

        buffer.close();
    }
}
