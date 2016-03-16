/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {


    /*Testing strategy for: getTimespan
     * Input space for this method is across the different tweet.timestamps that the list of distinct IDs can contain
     * Partitions: 
     *  List.length = 0, 1, >1
     *  Set of inputs where all tweets have distinct timestamps
     *  Set of inputs where two or more tweets share a timestamp
     *  Boundary case where all tweets have the same timestamp
     *  
     *  Testing strategy: We perform partition testing
     * 
     */
    
    /*Testing strategy for getMentionedUsers
     * Input space: 
     *      Length of list : 0, 1, > 1
     * Cases for usernames in body of text:
     *      No usernames mentioned
     *      More than one tweet with same username
     *      Multiple username mentions in same tweet
     *      Multiple mentions of same username but with different capitalization
     *      Text containing @ symbol which is not part of a username
     *      Case Insensitivity/underscores and other legal symbols
     *      @Taylor Herr - This is covered by the above statement. Multiple mentions of same username
     *      are covered by tests for diff capitalization because a set is returned
     * Testing Strategy: Part-based testing
     */
    
    private static final Instant D1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant D2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant D3 = Instant.parse("2016-02-17T12:00:00Z");


    
    private static final Tweet TWEET1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", D1);
    private static final Tweet TWEET2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", D2);
    private static final Tweet TWEET3 = new Tweet(3, "bbitdiddle", "@allCourse6! I don't understand why people dislike me so much. :( #bitdiddleProblems", D3);
    private static final Tweet TWEET4 = new Tweet(4, "alyssa", "I am basically Jack Florey, but more annoying @allcourse6. @@anyone_who-cares", D3);
    private static final Tweet TWEET5 = new Tweet(5, "krotus", "Look upon me, ye undergrad, and despair. @!lol @krotus@mit.edu" , D3);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    /* Start tests for Timespan*/
    
    //Test 0 (staff-provided)
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(TWEET2, TWEET1));
        
        assertEquals("expected start", D1, timespan.getStart());
        assertEquals("expected end", D2, timespan.getEnd());
    }

    //Test input where input list is empty
    @Test
    public void testGetTimespanInputEmptyList(){
        Timespan timespan = Extract.getTimespan(new ArrayList<Tweet>(1));
        assertTrue("start of timespan is before end", timespan.getStart().isBefore(timespan.getEnd()) || timespan.getStart() == timespan.getEnd());
    }
 
    //Test list of 1 tweet that has the same start and end
    @Test
    public void testGetTimespanOneTweet(){
        Timespan timespan = Extract.getTimespan(Arrays.asList(TWEET1));
        
        assertEquals("expected start", D1, timespan.getStart());
        assertEquals("expected end", D1, timespan.getEnd());
    }
    
    //Test list of more than two tweets, all with distinct timestamps, also test for list not modified
    @Test 
    public void testGetTimespanDistinctTimestampsListNotModified(){
        List<Tweet> tweetsList = Arrays.asList(TWEET1, TWEET3, TWEET2);
        Timespan timespan = Extract.getTimespan(tweetsList);
        
        assertEquals("expected start", D1, timespan.getStart());
        assertEquals("expected end", D3, timespan.getEnd());
        assertTrue("list not modified", tweetsList.equals(Arrays.asList(TWEET1, TWEET3, TWEET2)));
    }
    //Test list of more than two tweets, two with same timestamp
    @Test
    public void testGetTimespanSharedTimestamp(){
        Timespan timespan = Extract.getTimespan(Arrays.asList(TWEET1, TWEET3, TWEET4));

        assertEquals("expected start", D1, timespan.getStart());
        assertEquals("expected end", D3, timespan.getEnd());
    }
    //Test list of more than two tweets, all with same timestamp
    @Test
    public void testgetTimespanAllSameTimestamp(){
        Timespan timespan = Extract.getTimespan(Arrays.asList(TWEET3, TWEET4, TWEET5));
        
        assertEquals("expected start", D3, timespan.getStart());
        assertEquals("expected end", D3, timespan.getStart());
        
    }
    /* End tests for Timespan */
    
    /* Start tests for getMentionedUsers */
    //Test 0 (provided by staff)
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(TWEET1));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    //Test for empty input array
    @Test
    public void testGetMentionedUsersEmptyInput() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList());

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    //Test 1 only one input to list
    @Test
    public void testGetMentionedUsersOneTweet(){
       Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(TWEET3));
       Set<String> lowercaseMentionedUsers = convertSetElementsToLowercase(mentionedUsers);
       assertTrue("expected one username returned", lowercaseMentionedUsers.size() == 1);
       assertTrue("expected to contain allcourse6", lowercaseMentionedUsers.contains("allcourse6"));
        
    }
    //Test 2 more than one tweet with same username, but different capitalization
    @Test
    public void testGetMentionedUsersDifferentCapitalization(){
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(TWEET3, TWEET4));
        Set<String> lowercaseMentionedUsers = convertSetElementsToLowercase(mentionedUsers);
        assertTrue("expected size of output set to be two", lowercaseMentionedUsers.size() == 2);
        assertTrue("expected to contain allcourse6 and anyone_who-cares", lowercaseMentionedUsers.containsAll(Arrays.asList("allcourse6", "anyone_who-cares")));
    }
    //Test 3 tweet containing @ symbol as part of non-username, also list not modified
    @Test
    public void testGetMentionedUsersAtSymbolNotUserMentionListNotModified(){
        List<Tweet> tweetsList = Arrays.asList(TWEET3, TWEET5);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(TWEET3, TWEET5));
        Set<String> lowercaseMentionedUsers = convertSetElementsToLowercase(mentionedUsers);
        assertTrue("expected size of output set to be one", lowercaseMentionedUsers.size() == 2);
        assertTrue("expected to contain allcourse6, krotus", lowercaseMentionedUsers.containsAll(Arrays.asList("krotus", "allcourse6")));
        assertTrue("list supposed to not be modified", tweetsList.equals(Arrays.asList(TWEET3, TWEET5)));
    }
    /**
     * Takes in set of strings, returns set of same strings in lowercase
     * @param inputSet set of strings
     * @return set containing same words as inputSet, but lowercase
     */
    
    public static Set<String> convertSetElementsToLowercase(Set<String> inputSet){
        Set<String> returnedSet = new HashSet<>();
        for (String user: inputSet){
            returnedSet.add(user.toLowerCase());
        }
        return returnedSet;
    }
    
    /* End tests for getMentionedUsers */
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
