import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static java.util.Collections.max;

public class WordRecommender {

    private HashMap<Integer, ArrayList<String>> dictWords = new HashMap<Integer, ArrayList<String>>();

    public WordRecommender(String dictionaryFile) {
        // Load dictionary words into dictWords
        FileInputStream input;
        Scanner scn;

        try {
            input = new FileInputStream(dictionaryFile);
            scn = new Scanner(input);
            while (scn.hasNextLine()) {
                String word = scn.nextLine();
                int len = word.length();
                if (dictWords.containsKey(len)) {
                    dictWords.get(len).add(word);
                } else {
                    dictWords.put(len, new ArrayList<String>());
                    dictWords.get(len).add(word);
                }
            }
            scn.close();
        } catch (IOException e) {
            System.out.printf(Util.FILE_OPENING_ERROR);
        }

    }

    public HashMap<Integer, ArrayList<String>> getDictWords() {
        return dictWords;
    }
  
    public double getSimilarity(String word1, String word2) {
        // get shorter length of 2 string lengths
        int shorter = Math.min(word1.length(), word2.length());
        // calculate left similarity
        double leftSim = 0.0;
        for (int i = 0; i < shorter; i++) {
            char left = word1.charAt(i);
            char right = word2.charAt(i);
            if (left == right) {
                leftSim++;
            }
        }
        // calculate right similarity
        double rightSim = 0.0;
        for (int i = 0; i < shorter; i++) {
            char left = word1.charAt(word1.length() - i - 1);
            char right = word2.charAt(word2.length() - i - 1);
            if (left == right) {
                rightSim++;
            }
        }

        double result = (leftSim + rightSim) / 2.0;
        return result;
    }

    public double getCommonPercent(String word1, String word2) {
        HashSet<Character> intersection = new HashSet<Character>();
        HashSet<Character> union = new HashSet<Character>();

        for (int i = 0; i < word1.length(); i++) {
            char c = word1.charAt(i);
            String s = Character.toString(c);
            if (word2.contains(s)) {
                intersection.add(c);
            }
            union.add(c);
        }

        for (int i =  0; i < word2.length(); i++) {
            char c = word2.charAt(i);
            union.add(c);
        }

        return (1.0 * intersection.size() / union.size());
    }


    // function to get valid word replacement candidates: outputs a hashmap where keys are
        // left-right similarity values and values are an array list of dict words who has
        // the corresponding similarity values
    public HashMap<Double, ArrayList<String>> getValidCandidates(String word, int tolerance, double commonPercent) {

        HashMap<Double, ArrayList<String>> result = new HashMap<Double, ArrayList<String>>();
        int len = word.length();

        // checking only words of valid length
        for (int i = len - tolerance; i <= len + tolerance; i++) {
            // if words of length i exist
            if (dictWords.containsKey(i)) {
                ArrayList<String> sublist = dictWords.get(i);
                for (int j = 0; j < sublist.size(); j++) {
                    double commonP = getCommonPercent(word, sublist.get(j));
                    // checking only words of valid commonPercent
                    if (commonP >= commonPercent) {
                        double similarity = getSimilarity(word, sublist.get(j));
                        if (result.containsKey(similarity)) {
                            result.get(similarity).add(sublist.get(j));
                        } else {
                            result.put(similarity, new ArrayList<String>());
                            result.get(similarity).add(sublist.get(j));
                        }
                    }
                }
            }
        }

        return result;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {

        ArrayList<String> result = new ArrayList<String>();
        HashMap<Double, ArrayList<String>> candidates = getValidCandidates(word, tolerance, commonPercent);
        int len = word.length();
        int count = topN;

        // Rank topN valid replacements based on similarity
        while (count > 0 && !candidates.isEmpty()) {
            double maxSimilarity = max(candidates.keySet());
            ArrayList<String> replacements = candidates.remove(maxSimilarity);
            // if there are more valid replacements than spaces left in topN list
            if (replacements.size() >= count) {
                for (int i = 0; i < count; i++) {
                    result.add(0,replacements.get(i));
                }
                break;
            } else {
            // if there are fewer valid replacements than spaces left in topN list
                for (int i = 0; i < replacements.size(); i++) {
                    result.add(0,replacements.get(i));
                }
                count -= replacements.size();
            }
        }

        return result;
    }
  
    
  }