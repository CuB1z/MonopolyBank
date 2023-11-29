public class Utils {
    public static String getFilePath(String relativePath) {
        String absolutePath = System.getProperty("user.dir");
        return absolutePath + relativePath;
    }
}
