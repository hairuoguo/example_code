/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {
    
    private static final Instant D1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant D2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant D3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet TWEET1 = new Tweet(1, "alYssa", "is it reasonable to talk about rivest so much?", D1);
    private static final Tweet TWEET2 = new Tweet(2, "bbitDiddle", "rivest talk in 30 minutes #hype", D2);
    private static final Tweet TWEET3 = new Tweet(3, "bbitdIddle", "@allCourse6, @undergrads I don't understand why people dislike me so much. :( #bitdiddleProblems", D3);
    private static final Tweet TWEET4 = new Tweet(4, "alYssa", "@allcourse6 @anyone_who-cares I am basically Jack Florey, but more annoying.", D3);
    private static final Tweet TWEET5 = new Tweet(5, "Krotus", "@undergrads Look upon me, ye undergrad, and despair. krotus@mit.edu", D3);
    private static final Tweet TWEET6 = new Tweet(5, "Krotus", "@krotus, @undergrads I am referring to myself", D3);

    
    /* Testing Strategy for GuessFollowsGraph
     * Input space: List length = 0, 1, >1
     * Output space: Nobody follows anyone, only one person follows others, many people follow others
     * Testing strategy: we cover all partitioned spaces, can only test for 
     * expected results greater than an expected number because of the underdetermined return spec
     * @Tarek Mansour: This explanation makes the upcoming code easier to read/comprehend.
     */
    
    /* Testing Strategy for influencers
     * Input space: number of people in network = 1, >1
     * Persons in network have 0 follows, more than 0 follows.
     * Output space: no ties, some ties, all ties
     * 
     * Testing strategy: we cover all partitioned spaces
     */
    
    /**
     * Takes in list of strings, outputs them in same order but lowercase
     * @param list of strings
     * @return list of strings in same order, but lowercase
     */
    public static List<String> convertListElementsToLowercase(List<String> inputList){
        List<String> returnedList = new ArrayList<>(inputList.size());
        for (String user: inputList){
            returnedList.add(user.toLowerCase());
        }
        return returnedList;
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /* Start tests for GuessFollowsGraph() */

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    //Test 1: One tweet, that person follows many people
    @Test
    public void testGuessFollowsOneTweetMultipleFollows(){
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(TWEET3));
        followsGraph = convertMapLowercase(followsGraph);
        assertTrue("bbitdiddle is in map", followsGraph.containsKey("bbitdiddle"));
        assertTrue("following at least two people", followsGraph.get("bbitdiddle").size() >= 2);
        assertTrue("contains allcourse6 and undergrads", followsGraph.get("bbitdiddle").containsAll(Arrays.asList("allcourse6", "undergrads")));
         
    }
    //Test 2: More than one tweet, many people following different people, also test inputs not modified
    @Test
    public void testGuessMultipleTweetsMultipleFollowsListNotModified(){
        List<Tweet> tweetList = Arrays.asList(TWEET1, TWEET2, TWEET3, TWEET4, TWEET5);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweetList);
        followsGraph = convertMapLowercase(followsGraph);
        boolean containsAllExpected = followsGraph.keySet().containsAll(Arrays.asList("krotus", "bbitdiddle", "alyssa"));
        assertTrue("expected three people who follow people", containsAllExpected);
        assertTrue("expected two or more follows for alyssa", followsGraph.get("alyssa").size() >= 2 );
        assertTrue("expected two more follows for bbitdiddle", followsGraph.get("alyssa").size() >= 2);
        assertTrue("expected one or more follows for krotus", followsGraph.get("alyssa").size() >= 1);
        assertTrue("list not modified", tweetList.equals(Arrays.asList(TWEET1, TWEET2, TWEET3, TWEET4, TWEET5)));
    }
    //Test 3: Make sure that author isn't included 
    @Test
    public void testGuessFollowsAuthorMentioned(){
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(TWEET6));
        followsGraph = convertMapLowercase(followsGraph);
        assertFalse("expected following doesn't contain krotus", followsGraph.get("krotus").contains("krotus") );
    }
    /* End tests for GuessFollowsGraph() */
    
    /* Start tests for testInfluencersEmpty() */
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> lowercaseInfluencers = convertListElementsToLowercase(influencers);
        assertTrue("expected empty list", lowercaseInfluencers.isEmpty());
    }
    
    //Test: One person in network
    @Test
    public void testInfluencersOnePersonOneFollow(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("jack_florey", new HashSet<String>(Arrays.asList("krotus")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> lowercaseInfluencers = convertListElementsToLowercase(influencers);
        assertTrue("expected length of list is 2", lowercaseInfluencers.size() == 2);
    }
    //Test: Three people in network, same number of followers
    @Test
    public void testInfluencersAllTiedCaseInsensitive(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("person1", new HashSet<String>(Arrays.asList("jack_florey", "krotus", "sport_death")));
        followsGraph.put("person2", new HashSet<String>(Arrays.asList("jack_florey", "krOtus", "sport_death")));
        followsGraph.put("person3", new HashSet<String>(Arrays.asList("jack_florey", "krotUs", "spOrt_deAth")));
        followsGraph.put("jack_florey", new HashSet<String>(Arrays.asList("persOn1", "person2", "peRson3")));
        followsGraph.put("sport_death", new HashSet<String>(Arrays.asList("person1", "perSon2", "person3")));
        followsGraph.put("krotus", new HashSet<String>(Arrays.asList("person1", "person2", "PERSON3")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> lowercaseInfluencers = convertListElementsToLowercase(influencers);
        assertTrue("expected length of list is 6", lowercaseInfluencers.size() == 6);
    }
    //Test: Three people in network, two are tied, also test for inputs not modified
    @Test
    public void testInfluencersOneTieInputsNotModifiedCaseInsensitive(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Map<String, Set<String>> followsGraphClone = new HashMap<>();
        followsGraph.put("peRson1", new HashSet<String>(Arrays.asList()));
        followsGraph.put("person2", new HashSet<String>(Arrays.asList("jack_florey", "krOtus", "sport_death")));
        followsGraph.put("person3", new HashSet<String>(Arrays.asList("jack_fLorey", "kroTus", "perSon1")));
        followsGraphClone.put("peRson1", new HashSet<String>(Arrays.asList()));
        followsGraphClone.put("person2", new HashSet<String>(Arrays.asList("jack_florey", "krOtus", "sport_death")));
        followsGraphClone.put("person3", new HashSet<String>(Arrays.asList("jack_fLorey", "kroTus", "perSon1")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> lowercaseInfluencers = convertListElementsToLowercase(influencers);
        
        assertTrue("expected index of sport_death is 2 or 3", lowercaseInfluencers.indexOf("sport_death") == 2 || lowercaseInfluencers.indexOf("sport_death") == 3);
        assertTrue("expected index of jack_florey is first or second", lowercaseInfluencers.indexOf("jack_florey") == 0 || influencers.indexOf("jack_florey") == 1);
        assertTrue("expected index of krotus is first or second", lowercaseInfluencers.indexOf("krotus") == 0 || influencers.indexOf("krotus") == 1);
        assertTrue("expected index of person1 is 2 or 3", lowercaseInfluencers.indexOf("person1") == 2 || lowercaseInfluencers.indexOf("person1") == 3);
        assertTrue("map not modified", followsGraph.equals(followsGraphClone));
    }
    //Test: Test for input list of people following nobody
    @Test
    public void testInfluencersNoFollows(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Map<String, Set<String>> followsGraphClone = new HashMap<>();
        followsGraph.put("person1", new HashSet<String>(Arrays.asList()));
        followsGraph.put("person2", new HashSet<String>(Arrays.asList()));
        followsGraph.put("person3", new HashSet<String>(Arrays.asList()));
        followsGraphClone.put("person1", new HashSet<String>(Arrays.asList()));
        followsGraphClone.put("person2", new HashSet<String>(Arrays.asList()));
        followsGraphClone.put("person3", new HashSet<String>(Arrays.asList()));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        List<String> lowercaseInfluencers = convertListElementsToLowercase(influencers);
        assertTrue("size of influencers is 3", lowercaseInfluencers.size() == 3);
        assertTrue("contains persons 1 through 3", lowercaseInfluencers.containsAll(Arrays.asList("person1", "person2","person3")));
        assertTrue("map not modified", followsGraph.equals(followsGraphClone));
    }
    
    /**
     * Takes in a social network map and returns same map but with all keys lowercase
     * @param socialNetwork Map as defined in spec of class SocialNetwork
     * @return socialNetwork map but with all entries
     */
    //@Tarek Mansour - This is a new method and therefore needs its own specification.
    
    public static Map<String, Set<String>> convertMapLowercase(Map<String, Set<String>> socialNetwork){
        Map<String, Set<String>> newSocialNetwork = new HashMap<String, Set<String>>();
        Set<String> keys = socialNetwork.keySet();
        for (String key: keys){
            newSocialNetwork.put(key.toLowerCase(), ExtractTest.convertSetElementsToLowercase(socialNetwork.get(key)));
        }
        return newSocialNetwork;
    }
    /* End tests for testInfluencersEmpty() */

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
