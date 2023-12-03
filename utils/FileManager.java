package utils;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.io.*;

public class FileManager {
    public String getActualDate() {
        LocalDateTime date = LocalDateTime.now();
        String formatedDate;
        formatedDate = date.toString().split(".")[0];
        formatedDate = formatedDate.replace("T", "_");
        return formatedDate;
    }


    public static void saveFile(Object obj) {
        try {
            XMLEncoder encoder = new XMLEncoder(
                                    new BufferedOutputStream(
                                        new FileOutputStream("pelis.xml")));
            encoder.writeObject(obj);
            encoder.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error al guardar el archivo");
        }
    }
}
