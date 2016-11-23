/*
    Reads in a file and puts all of the bytes in an array.
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileReader {
    static byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    static HashMap<Integer, String> getImports(String filePath) {
        HashMap<Integer, String> ret = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            for (String s : stream.collect(Collectors.toList())) {
                String[] parts = s.split("@,"); //use pattern here instead of splits
                String funcName = parts[0].substring(8);
                int address = Integer.parseInt(parts[1].split(",")[0]);
                ret.put(address, funcName);
            }

        } catch (java.io.IOException e) {
            System.out.println("Couldn't open file");
        }

        return ret;
    }
}
