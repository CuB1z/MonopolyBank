// This class contains methods related to paths

public class PathUtils {
    public static String getFilePath(String relativePath) {
        String absolutePath = System.getProperty("user.dir");
        return absolutePath + relativePath;
    }
}
