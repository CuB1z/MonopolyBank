package src;

import java.io.*;
import java.util.*;
import utils.*;

public class Game implements Serializable{
    private String fileName;
    private MonopolyCode [] monopolyCodeArray = new MonopolyCode[Constants.MONOPOLY_CODE_ARRAY_SIZE];
    private Terminal terminal;
    private List<Player> players = new ArrayList<Player>();
 
    // BufferedReader to read the file (no need to close it)
    private BufferedReader buffer;

    // Constructor ========================================================================================================
    
    // Zero Constructor
    public Game() throws Exception {
        this.terminal = new TextTerminal();
    }
    
    // Public methods =====================================================================================================
    public void play() {

        // Initialize the game if the fileName is null
        if (this.fileName == null) this.initGame();

        // Show message
        this.terminal.show("Playing...");
        this.terminal.show("");

        // Save the game for the first time
        FileManager.saveFile(this, this.fileName);

        // Main Loop
        this.mainLoop();

        // Show the winner if the game is finished || Show the end game message
        if (this.isFinished()) {
            this.deleteFile(this.fileName);
            this.showWinner();
        }
        else this.showEndGame();
    }

    // Private methods ====================================================================================================

    //Method used to initialize the game
    private void initGame() {
       // Ask for language and set it
        this.askAndSetLanguage();
        this.terminal.flushScreen();

        // Create players
        this.createPlayers();
        this.terminal.flushScreen();

        // Load monopoly codes
        this.loadMonopolyCodes();
        
        // Set the fileName with the actual date
        this.setFileName(FileManager.getActualDate());
    }

    // Method used to execute the main loop
    private void mainLoop() {
        boolean finishGame = false;

        while (!this.isFinished() && !finishGame) {
            this.terminal.flushScreen();

            int answer = this.showMainLoopMenu();

            this.terminal.flushScreen();

            // Check the answer and execute the corresponding action
            switch (answer) {
                case Constants.CONTINUE_PLAYING_ID -> this.playTurn();
                case Constants.SHOW_SUMMARY_ID -> this.showSummary();
                case Constants.CHANGE_LANGUAGE_ID -> this.askAndSetLanguage();
                case Constants.EXIT_GAME_ID -> finishGame = true;
                default -> this.terminal.show("Invalid option");
            }

            // Save the game
            FileManager.saveFile(this, this.fileName);
        }
    }

    // Method used to play a turn
    private void playTurn() {
        
        // Ask for code ID and get mCode
        int codeId = this.getCodeId();
        MonopolyCode mCode = this.monopolyCodeArray[codeId];

        // Ask for player ID and get player
        int playerId = this.getPlayerId();
        Player player = this.players.get(playerId);

        if (mCode != null) mCode.doOperation(player);
        // if (mCode != null) this.execDoOperation(mCode, player);
        else {
            this.terminal.show("Invalid code");
            this.terminal.show("");
            player.showSummary();
        }

        this.terminal.waitForEnter();

        // Update players array
        if (player.isBankrupt()) this.players.remove(player);
    }

    // Method used to show the main loop menu
    private int showMainLoopMenu() {

        // Show the menu options
        this.terminal.show("Enter an option:");

        System.out.print(Constants.CONTINUE_PLAYING_ID + ": ");
        this.terminal.show("Continue playing");

        System.out.print(Constants.SHOW_SUMMARY_ID + ": ");
        this.terminal.show("Show summary");

        System.out.print(Constants.CHANGE_LANGUAGE_ID + ": ");
        this.terminal.show("Change language");

        System.out.print(Constants.EXIT_GAME_ID + ": ");
        this.terminal.show("Save and exit");

        this.terminal.show("");

        // Ask for the answer and check if it is valid
        while (true) {
            int answer = this.terminal.readInt();
            this.terminal.show("");

            if (answer < Constants.MAIN_LOOP_MIN_OPTION || answer > Constants.MAIN_LOOP_MAX_OPTION) {
                this.terminal.show("Invalid option");
                this.terminal.show("");

            } else return answer;
        }
    }

    //Method used to show the summary
    private void showSummary() {
        this.terminal.show("---Summary---");

        for (Player player : this.players) {
            player.showSummary();
            this.terminal.show("");
        }

        this.terminal.waitForEnter();
    }

    //Method used to get the code id
    private int getCodeId() {
        int codeId = Constants.MONOPOLY_CODE_ARRAY_SIZE;
            
        while (codeId > Constants.MONOPOLY_CODE_ARRAY_SIZE - 1 || codeId < 0) {
            this.terminal.show("Enter card code:");
            codeId = this.terminal.readInt();
            this.terminal.show("");

            if (codeId > Constants.MONOPOLY_CODE_ARRAY_SIZE - 1 || codeId < 0)
                this.terminal.show("Invalid code");
        }

        return codeId;
    }

    // Method used to get the player id
    private int getPlayerId() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        int playerId = Constants.MAX_NUMBER_OF_PLAYERS + 1;

        while (playerId > this.players.size() || playerId < 1) {

            String output = trs.translate("Enter player ID ( ");
            for (int i = 0; i < this.players.size(); i++) {
                String colorName = trs.translate(this.players.get(i).getColor().toString());
                output += i + 1 + ": " + colorName + " ";
            }
            output += ")";
            this.terminal.show(output);
            
            playerId = this.terminal.readInt();
            this.terminal.show("");

            if (playerId > this.players.size() || playerId < 1)
                this.terminal.show("Invalid ID");
        }

        return playerId - 1;
    }

    //Method used to show the winner
    private void showWinner() {
        Player player_winner = this.players.get(0);
        String winner = player_winner.getName();

        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        String output;
        output = trs.translate("Game Over >> %s is the winner!!");
        output = String.format(output, winner);

        this.terminal.flushScreen();
        this.terminal.show(output);
    }

    //Method used to show the end game message
    private void showEndGame() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        String output;
        output = trs.translate("The game has been saved in the file %s");
        output = String.format(output, this.fileName);

        this.terminal.flushScreen();
        this.terminal.show(output);
    }

    //Method used to initialize players
    private void createPlayers(){
        
        // Ask for the number of players
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = trs.translate("Enter the number of players (2-%d):");
        this.terminal.show(String.format(msg, Constants.MAX_NUMBER_OF_PLAYERS));

        int answer = this.terminal.readInt();
        this.terminal.show("");

        if (answer < 2 || answer > Constants.MAX_NUMBER_OF_PLAYERS) {
            this.terminal.show("Incorrect number of players");
            this.createPlayers();

        } else {
            // Translate the message
            msg = trs.translate("Enter the name of the player ");

            // Create the players
            String output = "";
            for (int i = 0; i < answer; i++) {
                this.terminal.show(msg + (i + 1) + ": ");
                String name = this.terminal.readStr();
                this.terminal.show("");

                this.players.add(new Player(i, name, this.terminal));
                output += this.players.get(i).toString() + "\n";
            }
            
            // Show the players
            this.terminal.flushScreen();
            this.showPlayers(output);
        }
    }

    // Method used to check if the game is finished
    private boolean isFinished() {
        return this.players.size() == 1;
    }

    // Method used to load the monopoly codes
    private void loadMonopolyCodes() {
        String file = PathUtils.getFilePath(Constants.MONOPOLY_CODE_FILE_PATH);

        // Configure the reader
        try {
            Reader in = new FileReader(file);
            this.buffer = new BufferedReader(in);

        } catch (FileNotFoundException e) {
            this.terminal.show("Error!!");
            System.exit(0);
        }

        this.terminal.show("Loading game data...");

        // Read the file line by line and initialize the monopolyCodeArray
        String configString = "";
        do {
            try {
                configString = buffer.readLine();

            } catch (IOException e) {
                this.terminal.show("Error!!");
                System.exit(0);
            }
            
            if (configString != null) {

                // Destructure the config line
                String [] info = this.destructureConfigLine(configString);

                // Assign the mCode
                this.assignMcode(info);
            }

        } while (configString != null);

        this.terminal.show("Data loaded correctly");
    }

    // Method used to assign the mCode
    private void assignMcode(String [] info) {
        String codeClass = this.getCodeClass(info);
        int codeId = this.getCodeId(info);

        // Create mCode based on the codeClass
        MonopolyCode mCode;
        switch (codeClass) {
            case Constants.PAYMENT_CHARGE_ID -> mCode = new PaymentCharge(info, this.terminal);
            case Constants.REPAIRS_ID -> mCode = new RepairsCard(info, this.terminal);
            case Constants.STATION_ID -> mCode = new Transport(info, this.terminal);
            case Constants.COMPANY_ID -> mCode = new Service(info, this.terminal);
            default -> mCode = new Street(info, this.terminal);
        }

        // Set the monopoly code in the array
        this.setMonopolyCode(codeId, mCode);
    }

    //Method used to set the monopoly code in the array
    private void setMonopolyCode(int id, MonopolyCode mCode) {
        this.monopolyCodeArray[id] = mCode;
    }

    //Method used to get the code class
    private String getCodeClass(String [] info) {
        return info[1];
    }

    //Method used to get the code id
    private int getCodeId(String [] info) {
        return Integer.parseInt(info[0]);
    }

    // Method used to destructure the config line
    private String [] destructureConfigLine(String config) {
        return config.split(Constants.DATA_SEPARATOR);
    }

    // Language methods ===================================================================================================

    // Ask desired language and return the index of the language
    private void askAndSetLanguage() {
        int answer = 0;
        boolean validAnswer = false;

        // Show available languages
        this.terminal.show("Available languages:");
        this.showAvailableLanguages();

        this.terminal.show("Enter the language you want to use:");

        // Ask for the language and check if it is valid
        while (!validAnswer) {
            answer = this.terminal.readInt();
            this.terminal.show("");

            if (answer < 0 || answer > Constants.AVAILABLE_LANGUAGES.length - 1) {
                this.terminal.show("Invalid value, enter another:");
                this.terminal.show("");

            } else validAnswer = true;
        }

        this.terminal.getTranslatorManager().setLanguage(Constants.AVAILABLE_LANGUAGES[answer]);
    }

    // Print available languages
    private void showAvailableLanguages() {
        this.terminal.show("0: " + Constants.DEFAULT_LANGUAGE_STRING_ID);

        for (int i = 1; i < Constants.AVAILABLE_LANGUAGES.length; i++) {
            Translator trs = this.terminal.getTranslatorManager().getTranslator();

            String lang = trs.translate(Constants.AVAILABLE_LANGUAGES[i]);
            String outString = String.format("%d: %s", i, lang);
            
            this.terminal.show(outString);
        }

        this.terminal.show("");
    }

    // Show the players
    private void showPlayers(String output) {
        this.terminal.show("---Players---");
        this.terminal.show(output);
        this.terminal.waitForEnter();
    }

    // Delete the file
    private void deleteFile(String fileName) {
        fileName = String.format(Constants.MONOPOLY_OLD_GAME, fileName);
        fileName = PathUtils.getFilePath(fileName);

        File file = new File(fileName);
        file.delete();
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