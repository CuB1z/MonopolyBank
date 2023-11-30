package utils;

/* This class contains the constants used in the game
    Asuming that { monopolyCode.txt format = (id;type;description) }
    Asuming that { monopolyCode.txt money format = ( charges (-amount) && payments (+amount) ) }
    Asuming that { languages/{language}.txt format = (default_language;translation) }

    If needed, the constants can be changed here
*/

public class Constants {
    // Paths
    public static final String MONOPOLY_CODE_FILE_PATH = "/config/monopolyCode.txt";
    public static final String LANGUAGES_FILE_PATH = "/config/languages/%s.txt";
    public static final String MONOPOLY_OLD_GAMES_PATH = "/config/oldGames";

    // Monopoly code constants
    public static final String PAYMENT_CHARGE_ID = "PAYMENT_CHARGE_CARD";
    public static final String REPAIRS_ID = "REPAIRS_CARD";

    public static final String STREET_ID = "STREET";
    public static final String STATION_ID = "TRANSPORT";
    public static final String COMPANY_ID = "SERVICE";

    public static final String DATA_SEPARATOR = ";";
    public static final String PRICE_DATA_SEPARATOR = "EUR";
    public static final String ALTERNATIVE_DATA_SEPARATOR = " ";


    // Default values
    public static final String DEFAULT_LANGUAGE = "spanish";

    // Players constants
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int INITIAL_BALANCE = 1500;

    // Available languages
    public static final String SPANISH = "spanish";
    public static final String ENGLISH = "english";
    
}