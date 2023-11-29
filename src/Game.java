import java.io.*;

import utils.*;

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
            String output = "";
            for (int i = 0; i < answer; i++) {
                this.terminal.show(msg + (i + 1) + ": ");
                String name = this.terminal.readStr();
                this.terminal.show("");

                this.players[i] = new Player(i, name, this.terminal);
                output += this.players[i].toString() + "\n";
            }

            // Show the players
            this.terminal.show("---Jugadores---");
            this.terminal.show(output);
        }
    }

    private void loadMonopolyCodes() throws Exception {
        String file = PathUtils.getFilePath(Constants.MONOPOLY_CODE_FILE_PATH);

        // Configure the reader
        Reader in = new FileReader(file);
        BufferedReader buffer = new BufferedReader(in);

        // Read the file line by line and initialize the monopolyCodeArray
        String configString;
        do {
            configString = buffer.readLine();
            
            if (configString != null) {

                // Destructure the config line
                String [] info = this.destructureConfigLine(configString);
                String codeClass = this.getCodeClass(info);
                int codeId = this.getCodeId(info);

                // Create mCode based on the codeClass
                MonopolyCode mCode;
                switch (codeClass) {
                    case Constants.PAYMENT_CHARGE_ID: mCode = new PaymentCharge(info, this.terminal); break;
                    case Constants.REPAIRS_ID: mCode = new RepairsCard(info, this.terminal); break;
                    case Constants.STATION_ID: mCode = new Transport(info, this.terminal); break;
                    case Constants.COMPANY_ID: mCode = new Service(info, this.terminal); break;
                    default: mCode = new Street(info, this.terminal); break;
                }

                // Set the monopoly code in the array
                this.setMonopolyCode(codeId, mCode);
            }

        } while (configString != null);

        buffer.close();
    }

    //Method used to set the monopoly code in the array
    private void setMonopolyCode(int id, MonopolyCode mCode) {
        this.monopolyCodeArray[id] = mCode;
    }

    //Method used to get the code class
    private String getCodeClass(String [] info) {
        String codeClass = info[1];
        return codeClass;
    }

    //Method used to get the code id
    private int getCodeId(String [] info) {
        int codeId = Integer.parseInt(info[0]);
        return codeId;
    }

    // Method used to destructure the config line
    private String [] destructureConfigLine(String config) {
        return config.split(Constants.DATA_SEPARATOR);
    }
}
