import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class WordRecommenderTest {

    private SpellChecker spellCheck;
    private WordRecommender wordRec;

    @BeforeEach
    void init() {
        wordRec = new WordRecommender("src/engDictionary.txt");
    }

    @Test
    void testCommonPercent0() {
        double commonPerc = wordRec.getCommonPercent("committee", "comet");
        double expect = 5.0/6.0;
        assertEquals(expect,commonPerc,0.01);
    }

    @Test
    void testCommonPercent1() {
        double commonPerc = wordRec.getCommonPercent("gardener", "nerdier");
        double expect = 4.0/7.0;
        assertEquals(expect,commonPerc,0.01);
    }

    @Test
    void testGetSimilarity0() {
        double similarity = wordRec.getSimilarity("aghast", "gross");
        double expect = 1.5;
        assertEquals(expect,similarity, 0.01);
    }

    @Test
    void testGetSimilarity1() {
        double similarity = wordRec.getSimilarity("oblige", "oblivion");
        double expect = 2.5;
        assertEquals(expect,similarity, 0.01);
    }

    @Test
    void testGetValidCandidates() {
        HashMap<Double, ArrayList<String>> candidates = wordRec.getValidCandidates("coonterrevolutionaries",0,0.5);
        HashMap<Double, ArrayList<String>> expect = new HashMap<Double, ArrayList<String>>();
        double similarity  = wordRec.getSimilarity("coonterrevolutionaries", "counterrevolutionaries");
        expect.put(similarity, new ArrayList<>());
        expect.get(similarity).add("counterrevolutionaries");
        assertEquals(expect,candidates);
    }


    @Test
    void testGetWordSuggestions0() {
        ArrayList<String> suggestions = wordRec.getWordSuggestions("morbit", 1, 0.7, 2);
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("orbit");
        expect.add("morbid");
        assertEquals(expect, suggestions);
    }

    @Test
    void testGetWordSuggestions1() {
        ArrayList<String> suggestions = wordRec.getWordSuggestions("ewook", 2, 0.5, 4);
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("awoke");
        expect.add("rework");
        expect.add("retook");
        expect.add("rebook");
        assertEquals(expect, suggestions);
    }

    @Test
    void testGetWordSuggestions2() {
        ArrayList<String> suggestions = wordRec.getWordSuggestions("xharacteristically", 0, 0.5, 4);
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("autobiographically");
        expect.add("unenthusiastically");
        expect.add("photosynthetically");
        expect.add("characteristically");
        assertEquals(expect, suggestions);
    }

    @Test
    void testGetWordSuggestions3() {
        ArrayList<String> suggestions = wordRec.getWordSuggestions("a", 1, 0.5, 5);
        ArrayList<String> expect = new ArrayList<String>();
        //ah, am, an, as
        expect.add("at");
        expect.add("as");
        expect.add("an");
        expect.add("am");
        expect.add("ah");
        assertEquals(expect, suggestions);
    }

    @Test
    void testGetWordSuggestions4() {
        ArrayList<String> suggestions = wordRec.getWordSuggestions("counterrevolutionari", 2, 0.9, 4);
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("counterrevolutionaries");
        expect.add("counterrevolutionary");
        assertEquals(expect, suggestions);
    }


}
