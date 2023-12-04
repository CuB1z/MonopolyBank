import java.io.*;
import java.util.*;

public class Game implements Serializable{
    private String fileName;
    private MonopolyCode [] monopolyCodeArray = new MonopolyCode[Constants.MONOPOLY_CODE_ARRAY_SIZE];
    private Terminal terminal;
    private List<Player> players = new ArrayList<Player>();
 
    // BufferedReader to read the file (no need to close it)
    private BufferedReader buffer;

    // Constructor ========================================================================================================
    public Game() throws Exception {
        this.terminal = new Terminal();
    }
    
    // Public methods =====================================================================================================
    public void play() {
        
        // Ask for language and set it
        int lang_id = this.askForLanguage();
        this.terminal.getTranslatorManager().setLanguage(Constants.AVAILABLE_LANGUAGES[lang_id]);

        // Create players
        this.createPlayers();
        
        // Load monopoly codes
        try {
            this.loadMonopolyCodes();

        } catch (Exception e) {
            this.terminal.show("Error!!");
            // Cancel the game
            System.exit(0);
        }

        this.terminal.show("Jugando...");

        // Set the fileName with the actual date
        this.setFileName(FileManager.getActualDate());

        while (!this.isFinished()) {
            // Ask for code ID
            this.terminal.show("Introduzca código de tarjeta:");
            int codeId = this.terminal.readInt();
            this.terminal.show("");

            // Ask for player ID
            this.terminal.show("Introduzca código de jugador (1: rojo 2: azul 3: verde 4: negro):");
            int playerId = this.terminal.readInt();
            this.terminal.show("");

            // Get monopolyCode and player
            MonopolyCode mCode = this.monopolyCodeArray[codeId];
            Player player = this.players.get(playerId - 1);

            // Cast the mCode variable and exec doOperation()
            if (mCode != null) {
                if (mCode instanceof Property){
                    Property property = (Property) mCode;
                    property.doOperation(player);
    
                } else if (mCode instanceof PaymentCharge){
                    PaymentCharge payCh = (PaymentCharge) mCode;
                    payCh.doOperation(player);
                    
                } else {
                    RepairsCard repCard = (RepairsCard) mCode;
                    repCard.doOperation(player);
                }

            } else this.terminal.show("Codigo invalido");

            // Update players array
            if (player.isBankrupt()) this.players.remove(player);

            FileManager.saveFile(this, this.fileName);
        }

        Player player_winner = this.players.get(0);
        String winner = player_winner.getName();

        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        String output;
        output = trs.translate("Fin del juego: %s es el ganador!!");
        output = String.format(output, winner);

        this.terminal.show(output);
    }

    // Private methods ====================================================================================================

    //Method used to initialize players
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

                this.players.add(new Player(i, name, this.terminal)) ;
                output += this.players.get(i).toString() + "\n";
            }

            // Show the players
            this.terminal.show("---Jugadores---");
            this.terminal.show(output);
        }
    }

    // Method used to check if the game is finished
    private boolean isFinished() {
        return this.players.size() == 1;
    }
    // Method used to load the monopoly codes
    private void loadMonopolyCodes() throws Exception {
        String file = PathUtils.getFilePath(Constants.MONOPOLY_CODE_FILE_PATH);

        // Configure the reader
        Reader in = new FileReader(file);
        this.buffer = new BufferedReader(in);

        this.terminal.show("Cargando datos del juego...");

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

        this.terminal.show("Datos cargados correctamente");
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

    // Language methods ===================================================================================================
    // Ask desired language and return the index of the language
    private int askForLanguage() {
        int answer = 0;
        boolean validAnswer = false;

        // Show available languages
        this.terminal.show("Lenguajes disponibles: ");
        this.showAvailableLanguages();

        this.terminal.show("Introduzca el lenguaje que desea utilizar: ");

        // Ask for the language and check if it is valid
        while (!validAnswer) {
            try {
                answer = this.terminal.readInt();
                this.terminal.show("");

                if (answer < 0 || answer > Constants.AVAILABLE_LANGUAGES.length - 1) {
                    this.terminal.show("Valor invalido, introduzca otro!!");
                    this.terminal.show("");

                } else
                    validAnswer = true;
            } catch (Exception e) {
                this.terminal.show("Introduzca un valor valido!!");
                this.terminal.show("");
            }
        }

        return answer;
    }

    // Print available languages
    private void showAvailableLanguages() {
        for (int i = 0; i < Constants.AVAILABLE_LANGUAGES.length; i++) {
            String outString = String.format("%d: %s", i, Constants.AVAILABLE_LANGUAGES[i]);
            this.terminal.show(outString);
        }

        this.terminal.show("");
    }

    // Getters & Setters ==================================================================================================
    public MonopolyCode [] getMonopolyCodeArray() {
        return this.monopolyCodeArray;
    }

    public void setMonopolyCodeArray(MonopolyCode [] monopolyCodeArray) {
        this.monopolyCodeArray = monopolyCodeArray;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}