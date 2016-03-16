/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    // Strategy for GraphPoet:
    //      Inputs: Text file with no contents, text file with string of words, text file with string of numbers, text file with
    //      multiple lines and punctuation
    //      We test for each of these partitions
    //
    // Strategy for poem():
    //      Inputs: Empty string, string bookended with spaces, string with uppercase letters
    //              string representing typical sentence, string with only one word, test involving corpus and poem with only symbols,
    //              input with multiple spaces between words
    //      We test for each of these cases
    //
    // Strategy for toString():
    //      Inputs: String representing empty corpus, string representing corpus of numbers
    //              string representing typical corpus (corpus of text organized in lines).
    //      We test for each of these cases
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    final private File providedCorpus = new File("test/poet/provided.txt");
    final private File emptyCorpus = new File("test/poet/empty.txt");
    final private File numbersCorpus = new File("test/poet/numbers.txt");
    final private File sampleCorpus = new File("test/poet/sample.txt");
    final private File symbolsCorpus = new File("test/poet/symbols.txt");

    
    @Test
    public void testGraphPoetLineWrapPunctuation() throws IOException{
        GraphPoet testPoet = new GraphPoet(sampleCorpus);
        String poem = testPoet.poem("What. hello lol");
        assertEquals("output poem should be as expected", "What. what hello what. lol", poem);
    }
    
    @Test
    public void testGraphPoetProvidedFile() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("Seek to explore new and exciting synergies!");
        assertEquals("output poem should be same as in example", "Seek to explore strange new life and exciting synergies!", poem);
    }
    
    @Test
    public void testGraphPoetProvidedFileMultipleSpaces() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("Seek to     explore new   \n and  exciting   synergies!");
        assertEquals("output poem should be same as in example", "Seek to explore strange new life and exciting synergies!", poem);
    }
    
    @Test
    public void testGraphPoetEmptyFile() throws IOException{
        GraphPoet testPoet = new GraphPoet(emptyCorpus);
        String poem = testPoet.poem("Seek to explore new and exciting synergies!");
        assertEquals("output poem should be same as input", "Seek to explore new and exciting synergies!", poem);
    }
    
    @Test
    public void testGraphPoetNumbersFile() throws IOException{
        GraphPoet testPoet = new GraphPoet(numbersCorpus);
        String poem = testPoet.poem("1 3");
        assertEquals("output poem should be 1 2 3", "1 2 3", poem);
    }
    
    @Test
    public void testGraphPoetSymbolsFile() throws IOException{
        GraphPoet testPoet = new GraphPoet(symbolsCorpus);
        String poem = testPoet.poem("@ $ % . ...");
        assertEquals("output poem should be as expected", "@ # $ % ! . .. ...", poem);
    }
    
    @Test
    public void testPoemEmptyString() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("");
        assertEquals("output string should be empty", "", poem);
    }
    
    @Test
    public void testPoemStringOnlySpaces() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("  ");
        assertEquals("output string should be empty", "", poem);
    }
    
    @Test
    public void testPoemStringBeforeAfterSpaces() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem(" Seek to explore new and exciting synergies! ");
        assertEquals("output poem should be same as in example", "Seek to explore strange new life and exciting synergies!", poem);
    }
    
    @Test
    public void testPoemOneWord() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("Seek");
        assertEquals("output poem should be same as input", "Seek", poem);
    }
    
    @Test
    public void testPoemRetainsCapitalsAndPunctuation() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String poem = testPoet.poem("SeEK to ExpLoRe nEw and exCiting sYnergies!");
        assertEquals("output poem should preserve capitalization", "SeEK to ExpLoRe strange nEw life and exCiting sYnergies!", poem);
    }
    
    
    
    @Test
    public void testToStringEmptyCorpus() throws IOException{
        GraphPoet testPoet = new GraphPoet(emptyCorpus);
        String returnedString = testPoet.toString();
        assertFalse("output poem should not be null", returnedString.equals(null));
    }
    
    @Test
    public void testToStringNumbersCorpus() throws IOException{
        GraphPoet testPoet = new GraphPoet(numbersCorpus);
        String returnedString = testPoet.toString();
        assertFalse("output poem should not be null", returnedString.equals(null));
    }
    
    @Test
    public void testToStringProvidedCorpus() throws IOException{
        GraphPoet testPoet = new GraphPoet(providedCorpus);
        String returnedString = testPoet.toString();
        assertFalse("output poem should not be null", returnedString.equals(null));
    }
}
