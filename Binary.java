import java.util.HashMap;

public class Binary {
    final static int ENTRY_POINT = 0x12A0 - 0x1000; //hello world
    final static int IAT_RVA = 0xE14C + 0x400000;
    final static int IAT_SIZE = 0xFC;
    final static int LOCATION_OF_EXITPROCESS = 0x70d0;
    final static String BINARY_PATH = "C:\\Users\\Ecoste\\IdeaProjects\\i-didn-t-think-of-a-name-yet\\helloWorld32.bin";
    final static String IMPORT_PATH = "C:\\Users\\Ecoste\\IdeaProjects\\i-didn-t-think-of-a-name-yet\\helloWorld32_importMacros.txt";

    static byte[] bytes;
    static HashMap<Integer, String> funcNames;

    private static final Binary instance = new Binary();

    private Binary() {
        bytes = FileReader.readFile(BINARY_PATH);
        funcNames = FileReader.getImports(IMPORT_PATH);
    }
}
