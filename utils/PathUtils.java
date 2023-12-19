package utils;

// Auxiliary class to get the absolute path of a file in the project

public class PathUtils {
    public static String getFilePath(String relativePath) {
        String absolutePath = System.getProperty("user.dir");
        return absolutePath + relativePath;
    }
}