import java.io.*;
import java.util.*;


public class SpellChecker {

    // Create instances adn variables
    private File dictFile, inputFile, outputFile;
    private String dictFilename, inputFilename, outputFilename;
    private HashSet<String> dictWords; // HashSet to store dictionary words
    private Scanner inputScanner;  // for scanning the input file
    private Printer outputPrinter; // for printing the output file
    private Scanner userInput;      // for read user's input
    private WordRecommender wordRec;

    // Default values for suggestion criteria
    private int tolerance = 2;
    private double commonPercent = 0.5;
    private int topN = 4;



    /**********************************************************************************************************************
    CONSTRUCTOR
 **********************************************************************************************************************/
    public SpellChecker() {
        userInput = new Scanner(System.in);
        inputDict();
        inputFile();
        dictFile = new File(dictFilename);
        inputFile = new File(inputFilename);
        outputFile = new File(outputFilename);
        wordRec = new WordRecommender(dictFilename);
    }

/**********************************************************************************************************************
  METHOD TO RUN THE SPELLCHECKER
 **********************************************************************************************************************/
    public void start() {
        loadDictionary();   // load dictionary file into Hashset
        loadScanner();      // load input file into scanner
        loadPrinter();      // load output file into printer
        spellCheck();       // do the actual spellcheck
    }

/**********************************************************************************************************************
  METHODS FOR INPUTTING FILES ( DICTIONARY AND THE FILE FOR SPELLCHECKING)
 **********************************************************************************************************************/
    public void inputDict() {
        while (true) {
            System.out.printf(Util.DICTIONARY_PROMPT);
            String userFilename = userInput.nextLine(); // get user's input filename
            if (validFilename(userFilename)) {          // Check if user's input filename is valid:
                this.dictFilename = userFilename;       // If valid, set the dictionary filename to the user input one
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, dictFilename);
                break;
            } else {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }

    public void inputFile() {
        while (true) {
            System.out.printf(Util.FILENAME_PROMPT);
            String userFilename = userInput.nextLine();     // get user's input filename
            if (validFilename(userFilename)) {              // Check if user's input filename is valid:
                this.inputFilename = userFilename;          // If valid, set the dictionary to the user input one.
                this.outputFilename = this.inputFilename.replaceFirst("(\\.[^.]+)$", "_chk$1"); // generate the output filename
                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, this.inputFilename, this.outputFilename);
                break;
            } else {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }

    /** Method to check if a filename is valid (has corresponding file exists) **/
    public static boolean validFilename(String filename) {
        File file = new File(filename);    // * this step does not actually check if the file exists, just create a path
        return file.canRead();             // The method 'canRead' checks if the file exists and can be read
    }

/**********************************************************************************************************************
    LOADERS: LOAD DICTIONARY FILE INTO HASHSET, LOAD THE INPUT FILE INTO SCANNER, LOAD THE OUTPUT FILE INTO PRINTER
 **********************************************************************************************************************/
    private void loadDictionary() {
        dictWords = new HashSet<>();                // loads dictionary into a HashSet for efficient lookup
        try (Scanner dictScanner = new Scanner(this.dictFile)) {
            while (dictScanner.hasNext()) {
                String word = dictScanner.next();
                dictWords.add(word.toLowerCase());  // Assuming case-insensitivity for dictionary words
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file not found: " + this.dictFilename);
            throw new RuntimeException(e);
        }
    }

    public void loadScanner() {
        try {
            this.inputScanner = new Scanner(this.inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("The input file was not found: " + this.inputFilename);
            throw new RuntimeException(e);
        }
    }

    public void loadPrinter() {
        try {
            this.outputPrinter = new Printer(this.outputFile);
            this.outputPrinter.clearFile();
        } catch (IOException e) {
            System.err.println("There was a problem initializing the printer for the output file: " + this.outputFilename);
            throw new RuntimeException(e);
        }
    }

/**********************************************************************************************************************
    METHODS THAT ACTUALLY PERFORM SPELLCHECK, (PROMPT USER, REPLACE WORD, AND PRINT ON THE INITIALIZED FILES)
 **********************************************************************************************************************/
    public void spellCheck() {
        while(inputScanner.hasNext()) {                 // while the input file has a next word to scan
            String currWord = inputScanner.next();      // scan that next word as the current word

            if(isInDict(currWord)) {                    // if the current word is in dictionary, meaning it is correctly spelled
                outputPrinter.appendWord(currWord);     // thus append the correctly spelled word in the output file.
            } else {                                    // else, if not in dict, it is spelled incorrectly
                ArrayList<String> suggestedWords = wordRec.getWordSuggestions(currWord, tolerance, commonPercent, topN);// get word suggestions
                // prompt user the current word is wrong, and let they know possible suggestions
                promptUserMisspell(currWord, suggestedWords);
                // get user's choice of word to print into the output file
                String userWord = getUserWordChoice(currWord, suggestedWords);
                outputPrinter.appendWord(userWord);
            }
        }
    }

    private void promptUserMisspell(String currWord, ArrayList<String> suggestedWords){
        System.out.printf(Util.MISSPELL_NOTIFICATION, currWord);
        if(suggestedWords.isEmpty()) {
            System.out.printf(Util.NO_SUGGESTIONS);
        } else {
            System.out.printf(Util.FOLLOWING_SUGGESTIONS);
//            System.out.printf(Util.SUGGESTION_ENTRY, suggestedWords.size(), suggestedWords);
            for (int i = 0; i < suggestedWords.size(); i++) {
                System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestedWords.get(suggestedWords.size() - i -1));
            }
        }
    }

    /** Method to Prompt and get user's choice on word to  replace the wrongly spelled word **/
    private String getUserWordChoice(String currWord, ArrayList<String> suggestedWords) {
        String chosenWord = "";
        while(Objects.equals(chosenWord, "")) {
            if(suggestedWords.isEmpty()) {                          // Prompt message should depend on whether word has suggestions or not
                System.out.printf(Util.TWO_OPTION_PROMPT);         // if the word does NOT have suggestions, only two options: "a" and "t"
            } else {System.out.printf(Util.THREE_OPTION_PROMPT); } // if the word does have suggestions, there are three option: 'r', 'a', 't'

            String entry = userInput.next();                        // get user's input entry
            /***************************************************************
             * if entry is "r", replace the word with one of the suggestions
             ***************************************************************/
            if(entry.equals("r") && !suggestedWords.isEmpty()) {
                System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
                while(true) {                                               // get the user input number entry and check if it is valid
                    int wordNum = userInput.nextInt();
                    if (1 <= wordNum && wordNum <= suggestedWords.size()) {
                        chosenWord = suggestedWords.get(suggestedWords.size() - wordNum);         // if valid, set it as the chosen word, then break the loop
                        break;
                    } else { System.out.printf(Util.INVALID_RESPONSE); }    // else, continue the loop, prompt the user to re-input
                }
            }
            /***************************************************************
             *  if entry is "a", accepts word as it is
             ***************************************************************/
            else if(entry.equals("a")) {
                chosenWord = currWord;
            }
            /***************************************************************
             * if entry is "t", let user enters a new word manually
             ***************************************************************/
            else if(entry.equals("t")) {
                System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                chosenWord = userInput.next();	// get a new word from user
            /****************************************************************************
             * else, the entry is invalid. Continue to loop to re-prompt user for entry.
             ****************************************************************************/
            } else {
                System.out.printf(Util.INVALID_RESPONSE);
            }
        }
        return chosenWord;
    }

    /** METHOD TO CHECK IF THE SCANNED WORD IS IN DICTIONARY **/
    public boolean isInDict(String scannedWord) {
        return dictWords.contains(scannedWord.toLowerCase()); // Match case-insensitivity
    }
}

