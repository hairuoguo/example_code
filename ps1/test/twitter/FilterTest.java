/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {


    /* Testing strategy for writtenBy
     * Input space: Length of list = 0, 1, >1
     * Output space: Number of tweets by author = 0, >=1
     * Testing strategy: we test by covering the parts for the partitions above
     */
    
    /* Testing strategy for inTimespan
     * Input space: Timespan of infinitesimal length, timespan of measurable length
     * Tweets on the border of the timespan, cases where no tweets are in timespan, cases where some tweets are in timespan
     * Case where there is an empty list of tweets, one tweet, case where there are multiple tweets
     * Testing strategy: performs cover of all partitioned inputs/outputs, and some boundary conditions
     */
    
    /* Testing strategy for testContaining
     * Input space: length of words list = 0, 1, >1
     *      length of tweets list = 0, 1, >1
     *      Case Insensitivity/Punctuation
     * Output space: number of tweets = 0, >=1
     * Testing strategy: performs cover of all partitioned inputs/outputs
     */
    private static final Instant D0 = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant D1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant D2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant D3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet TWEET1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", D1);
    private static final Tweet TWEET2 = new Tweet(2, "BBITDIDDLE", "rivest talk in 30 minutes #hype thiswordisnotunderstand", D2);
    private static final Tweet TWEET3 = new Tweet(3, "bbitdiddle", "@allCourse6 I don't UnDersTand why people dislike me so much. :( #bitdiddleProblems", D3);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    /* Start writtenBy() tests*/
    //Test: provided by staff
    @Test
    public void testWrittenByMultipleTweetsSingleResultCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(TWEET1, TWEET2), "ALYSSA");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(TWEET1));
    }
    //Test: test for input empty list and empty name string
    @Test
    public void testWrittenByEmptyListEmptyName(){
        List<Tweet> writtenBy = Filter.writtenBy(new ArrayList<Tweet>(1), "");
        
        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    //Test: Testing for when list contains no tweets by author, also covers list.length = 1
    @Test
    public void testWrittenByOneTweetNoResult(){
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(TWEET2), "alyssa");
        
        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    //Test: Testing for when list contains more than one tweet by author, also covers list.length > 1, also list not modified
    @Test
    public void testWrittenByMultipleTweetsMultipleResultsListNotModifiedCaseInsensitive(){
        List<Tweet> tweetList = Arrays.asList(TWEET1, TWEET2, TWEET3); 
        List<Tweet> writtenBy = Filter.writtenBy(tweetList, "bbitdiddle");

        assertEquals("expected two results", 2, writtenBy.size());
        assertTrue("list not modified", tweetList.equals(Arrays.asList(TWEET1, TWEET2, TWEET3)));
        assertTrue("expected list to contain two tweets by bbitdiddle", writtenBy.containsAll(Arrays.asList(TWEET2, TWEET3)));
    }
   
    
    /*End writtenBy() tests*/
    
    /*Start inTimespan() tests*/
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        //@Joshua Segaran - Done, Instances used multiple times made static final
        Instant testStart = D0;
        Instant testEnd = D3;
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(TWEET1, TWEET2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(TWEET1, TWEET2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(TWEET1));
    }
    @Test
    public void testInTimespanEmptyList(){
        Instant testStart = D0;
        Instant testEnd = D3;
 
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(testStart, testEnd));
        assertTrue("expected list size to be zero", inTimespan.size() == 0);
    }
    //Test 1: Multiple tweets in timespan of infinitesimal length
    @Test
    public void testInTimespanMutlipleTweetsInstantaneousTimespan(){
        Instant testStart = D3;
        Instant testEnd = D3;
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(TWEET3), new Timespan(testStart, testEnd));
        assertEquals("expected one result", 1, inTimespan.size());
        assertTrue("expect list to contain one tweet", inTimespan.contains(TWEET3));
        
    }
    //Test 2: Multiple tweets on border of timespan
    @Test
    public void testInTimespanMutlipleTweetsTimespanBorder(){
        Instant testStart = D1;
        Instant testEnd = D3;
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(TWEET1, TWEET2, TWEET3), new Timespan(testStart, testEnd));
        assertEquals("expected three results", 3, inTimespan.size());
        assertTrue("expect list to contain one tweet", inTimespan.containsAll(Arrays.asList(TWEET1, TWEET2, TWEET3)));
    }
    //Test 3: Case where none of the tweets are within timespan, also inputs not modified
    @Test
    public void testInTimespanMultipleTweetsNoResultInputsNotModified(){
        Instant testStart = Instant.parse("2016-02-17T07:00:00Z");
        Instant testEnd = D0;
        List<Tweet> tweetList = Arrays.asList(TWEET1, TWEET2, TWEET3);
        Timespan tweetsInterval = new Timespan(testStart, testEnd);
        List<Tweet> inTimespan = Filter.inTimespan(tweetList, tweetsInterval);
        assertEquals("expected zero results", 0, inTimespan.size());
        assertTrue("list not modified", tweetList.equals(Arrays.asList(TWEET1, TWEET2, TWEET3)));
        assertTrue("timespan not modified", tweetsInterval.equals(new Timespan(testStart, testEnd)));
        
    }
    //Test 4: Case where there is only 1 tweet
    public void testInTimespanSingleTweetSingleResult(){
        Instant testStart = Instant.parse("2016-02-17T09:30:00Z");
        Instant testEnd = Instant.parse("2016-02-17T10:30:00Z");
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(TWEET1), new Timespan(testStart, testEnd));
        assertEquals("expected one result", 1, inTimespan.size());
        assertTrue("expect list to contain one tweet", inTimespan.contains(TWEET1));
    }
    
    /*End inTimespan() tests*/
    
    /*Start containing() tests*/
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET1, TWEET2), Arrays.asList("tAlK"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(TWEET1, TWEET2)));
        assertEquals("expected same order", 0, containing.indexOf(TWEET1));
    }
    
    //Testing for empty list of tweets as input
    @Test
    public void testContainingTweetsEmptyList(){
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET1, TWEET2), Arrays.asList());
        assertTrue("expected non-empty list", containing.isEmpty());

    }
    
    //Testing for empty list of words as input
    @Test
    public void testContainingWordsEmptyList(){
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("tAlK"));
        assertTrue("expected non-empty list", containing.isEmpty());

    }
    
    
    //Test: More than one word, one tweet, multiple words in tweet input
    @Test
    public void testContainingMultipleWordsOneTweetOneResultCaseInsensitive(){
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET2, TWEET3), Arrays.asList("UNDERSTAND", "nothing", "nothing"));
        assertEquals("expected one result", 1, containing.size());
        assertTrue("expected list to contain one tweet", containing.contains(TWEET3));
    }
    //Test: No tweets in results, test inputs not modified
    @Test
    public void testContainingMultipleWordsAndTweetsNoResults(){
        List<Tweet> tweetList = Arrays.asList(TWEET1, TWEET2, TWEET3);
        List<String> wordsList = Arrays.asList("Lorem", "ipsum");
        List<Tweet> containing = Filter.containing(tweetList, wordsList);
        assertEquals("expected zero results", 0, containing.size());
    }
    //Test: Multiple words, multiple results
    @Test
    public void testContainingMultipleWordsAndTweetsMultipleResultsCaseInsensitive(){
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET1, TWEET2, TWEET3), Arrays.asList("uNderStand", "talk"));
        assertEquals("expected three results", 3, containing.size());
        assertTrue("expected list to contain three tweets", containing.containsAll(Arrays.asList(TWEET1, TWEET2, TWEET3)));
        assertEquals("expected same order", 0, containing.indexOf(TWEET1));
        assertEquals("expected same order", 1, containing.indexOf(TWEET2));
        
    }
    //Test: Test for contains word as a substring of another word
    @Test
    public void testContainingWordIsSubstringCaseInsensitive(){
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET1, TWEET2, TWEET3), Arrays.asList("undErstAnd"));
        assertEquals("expected three results", 1, containing.size());
        assertTrue("expected list contains tweet3", containing.contains(TWEET3));
    }
    //Test: Test for search word contains punctuation
    @Test
    public void testContainingWordHasPunctuationCaseInsensitive(){
        List<Tweet> containing = Filter.containing(Arrays.asList(TWEET1, TWEET2, TWEET3), Arrays.asList("#bitdiDdleproblems"));
        assertEquals("expected 1 result", 1, containing.size());
        assertTrue("expected list contains tweet3", containing.contains(TWEET3));

    }
    
    /*End containing() tests*/
    
    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
