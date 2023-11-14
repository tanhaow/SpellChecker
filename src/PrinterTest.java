import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PrinterTest {

    private SpellChecker spellCheck;
    private Printer printer;
    File outputFile;
    String filePath;

    @BeforeEach
    void init() throws IOException {
        outputFile = new File("src/output.txt");
        filePath = outputFile.getPath();
        printer = new Printer(outputFile);
        printer.clearFile();
    }

    @Test
    void testAppendWord0() throws IOException {
        printer.appendWord("Hello");
        printer.appendWord("I");
        printer.appendWord("am");
        printer.appendWord("Doraemon");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String actualContent = reader.readLine();
        String expectedContent = "Hello I am Doraemon ";
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testAppendWord1() throws IOException {
        printer.appendWord("Hello");
        printer.appendWord("Doraemon,");
        printer.appendWord("this");
        printer.appendWord("is");
        printer.appendWord("Dora-nichov");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String actualContent = reader.readLine();
        String expectedContent = "Hello Doraemon, this is Dora-nichov ";
        assertEquals(expectedContent, actualContent);
    }

}