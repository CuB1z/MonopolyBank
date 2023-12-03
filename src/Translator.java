import java.util.Map;
import java.util.HashMap;
import java.io.*;
import utils.PathUtils;

public class Translator implements Serializable {
    private String language;
    private Map<String, String> dictionary = new HashMap<String, String>();

    // Constructor ========================================================================================================
    public Translator(String filename) throws Exception {
        
        // Get the absolute path of the file
        filename = PathUtils.getFilePath(filename);

        // Get the language from the filename
        String rootFile = filename.split("/")[filename.split("/").length - 1];
        this.language = rootFile.split("\\.")[0];

        // Read the file and complete the dictionary
        Reader in = new FileReader(filename);
        BufferedReader buffer = new BufferedReader(in);

        String line;
        do {
            line = buffer.readLine();

            if (line != null) {
                this.dictionary.put(line.split(",")[0], line.split(",")[1]);
            }
            
        } while (line != null);

        buffer.close();
    }

    // Public methods =====================================================================================================
    public String translate(String word) {
        if (!this.dictionary.containsKey(word)) {
            return word;
        }

        return this.dictionary.get(word);
    }

    // Getters & setters ==================================================================================================
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getDictionary() {
        return this.dictionary;
    }

    public void setDictionary(Map<String, String> dictionary) {
        this.dictionary = dictionary;
    }
}
