/*
    Reads in a file and puts all of the bytes in an array.
 */

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileReader {
    byte[] data;

    FileReader(String filePath) {
        try {
            data = Files.readAllBytes(Paths.get(filePath));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
