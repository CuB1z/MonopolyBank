import java.io.*;

import utils.Constants;
import utils.PathUtils;

public class Game implements Serializable{
    private MonopolyCode [] monopolyCodeArray = new MonopolyCode[80];

    // Constructor ========================================================================================================
    public Game() throws Exception {
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
        // TODO Implement createPlayers method
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
