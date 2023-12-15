/* This class contains the constants used in the game
    Asuming that { monopolyCode.txt format = (id;type;description) }
    Asuming that { monopolyCode.txt money format = ( charges (-amount) && payments (+amount) ) }
    Asuming that { languages/{language}.txt format = (default_language;translation) }

    If needed, the constants can be changed here
*/

public class Constants {
    
    // Default values ===============================================================
    public static final String DEFAULT_LANGUAGE = "spanish";
    public static final String DEFAULT_GAMES_EXTENSION = ".xml";

    // Default aproval string should always be lowercase so toLowerCase() can be used
    public static final String DEFAULT_APROVE_STRING = "s";

    public static final int DEFAULT_CANCEL_INT = 0;

    // Paths ========================================================================
    public static final String MONOPOLY_CODE_FILE_PATH = "/config/monopolyCode.txt";
    public static final String LANGUAGES_FILE_PATH = "/config/languages/%s.txt";
    public static final String MONOPOLY_OLD_GAMES_PATH = "/config/oldGames";
    public static final String MONOPOLY_OLD_GAME = "/config/oldGames/%s" + DEFAULT_GAMES_EXTENSION;

    // Monopoly code constants ======================================================
    public static final String PAYMENT_CHARGE_ID = "PAYMENT_CHARGE_CARD";
    public static final String REPAIRS_ID = "REPAIRS_CARD";

    public static final String STREET_ID = "STREET";
    public static final String STATION_ID = "TRANSPORT";
    public static final String COMPANY_ID = "SERVICE";

    public static final String DATA_SEPARATOR = ";";
    public static final String PRICE_DATA_SEPARATOR = "EUR";
    public static final String ALTERNATIVE_DATA_SEPARATOR = " ";

    public static final int MONOPOLY_CODE_ARRAY_SIZE = 81;

    // Monopoly Rules Constants =====================================================
    public static final int MAX_NUMBER_OF_HOUSES = 5;
 
    // Players constants ============================================================
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int INITIAL_BALANCE = 1500;

    // Game constants ===============================================================
    public static final int MAIN_LOOP_MIN_OPTION = 1;
    public static final int MAIN_LOOP_MAX_OPTION = 4;

    public static final int CONTINUE_PLAYING_ID = 1;
    public static final int SHOW_SUMMARY_ID = 2;
    public static final int CHANGE_LANGUAGE_ID = 3;
    public static final int EXIT_GAME_ID = 4;
    
    // Available languages ==========================================================
    public static final String[] AVAILABLE_LANGUAGES = {"spanish", "english"};
    
}