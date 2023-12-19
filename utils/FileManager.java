package utils;

/*
    Auxiliary class containing the methods to save and read files

    - saveFile: Method to save a game in a file
    - readFile: Method to read a game from a file
    - getActualDate: Method to get the actual date in the format YYYY-MM-DD_hh-mm-ss

    Actual date is used as a file name to avoid overwriting files errors
*/

import src.Game;
import java.time.LocalDateTime;
import java.beans.*;
import java.io.*;

public class FileManager {

    public static void saveFile(Game game, String fileName) {

        // Create finalPath with actualDate
        String filePath = String.format(Constants.MONOPOLY_OLD_GAME, fileName);
        String finalPath = PathUtils.getFilePath(filePath);

        //Try to save the file
        try {

            // Create the file if it doesn't exist
            File file = new File(finalPath);
            if (!file.exists()) file.createNewFile();

            // Configure the encoder
            XMLEncoder encoder = new XMLEncoder(
                                 new BufferedOutputStream(
                                 new FileOutputStream(finalPath))
                                );

            // Save the object
            encoder.writeObject(game);
            encoder.close();

        } catch (IOException e) {
            System.out.println("Error trying to save the file");
        }
    }

    public static Game readFile(String fileName) {

        // Create finalPath with fileName
        String filePath = String.format(Constants.MONOPOLY_OLD_GAME, fileName);
        String finalPath = PathUtils.getFilePath(filePath);

        // Try to read the file
        try {

            // Configure the decoder
            XMLDecoder decoder = new XMLDecoder(
                                 new BufferedInputStream(
                                 new FileInputStream(finalPath))
                                );

            // Read the object
            Game game = (Game) decoder.readObject();
            decoder.close();
            return game;

        } catch (IOException e) {
            System.out.println("Error");
            System.out.println(e);
            return null;
        }
    }

    // Method to get the actual date in the format yyyy-MM-dd_HH-mmss (File Name)
    public static String getActualDate() {
        LocalDateTime date = LocalDateTime.now();

        String formatedDate;
        formatedDate = date.toString().split("\\.")[0];
        formatedDate = formatedDate.replace("T", "_").replace(":","-");

        return formatedDate;
    }
}