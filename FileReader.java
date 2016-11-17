/*
    Reads in a file and puts all of the bytes in an array.
 */

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileReader {
    static byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
