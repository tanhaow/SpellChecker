import java.io.*;

public class Printer {
    private File outputFile;
    private PrintWriter printWriter;

    // Constructs a Printer to write to the specified file
    public Printer(File outputFile) throws IOException {
        this.outputFile = outputFile; // pass in the output file to be printed
        openFile(); // Keep the file open for future appending text
    }

    // Opens the file in append mode
    private void openFile() throws IOException {
        FileWriter fw = new FileWriter(this.outputFile, true);
        printWriter = new PrintWriter(new BufferedWriter(fw));
    }

    // Appends the passed word into the file
    public void appendWord(String word) {
        printWriter.print(word + " "); // Prints the word with a trailing space
        printWriter.flush(); // Ensures the word is written to the file
    }

    // Clears the file's contents
    public void clearFile() throws IOException {
        printWriter.close(); // Closes the current PrintWriter
        // Effectively clear/overwrite the file's content by reopenning it
        printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.outputFile.getName())));
        printWriter.flush(); // Flush the stream to confirm it's clear
    }

    // Closes the PrintWriter, releasing any system resources associated with it
    public void close() { if (printWriter != null) { printWriter.close(); } }

    // Ensures PrintWriter is closed when Printer is garbage-collected.
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
