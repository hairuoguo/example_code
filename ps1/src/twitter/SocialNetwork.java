/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {
    
    
 
    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> socialNetwork = new HashMap<>();
        for (Tweet tweet: tweets){// for each tweet, get author, if author not already in dict, extract all mentioned authors and add to map
            String author = tweet.getAuthor().toLowerCase(); // because we turn everything lowercase to compare
            if (!socialNetwork.containsKey(author)){
                List<Tweet> writtenByAuthor = Filter.writtenBy(tweets, author);
                Set<String> usernameMentions = Extract.getMentionedUsers(writtenByAuthor);
                usernameMentions.remove(author);// we do this just in case the tweet mentions the author themselves
                socialNetwork.put(author, usernameMentions);
            }
        }
        Map<String, Set<String>> newSocialNetwork = triadicClosure(socialNetwork, 2); //call the triadic closure function for inferring more influencers
        return newSocialNetwork;
    }
    /**
     * This function takes in a social network map and performs triadic 
     * closure if at least thresholdFirstInfluencers intermediaries are shared. E.g., If A follows B, B follows C, and
     * A follows D, D also follows C, then A is influenced by C
     * @param basicMap takes in map according to the standards set by the specification under the class SocialNetwork 
     * @param thresholdFirstInfluencers: number of intermediary followers between two users needed to determine influence
     *      value must be 1 or greater.
     * @return returns an updated map using the SocialNetwork standards, where all author names are lowercase
     */
    public static Map<String, Set <String>> triadicClosure(Map<String, Set<String>> basicMap, final int thresholdFirstInfluencers){
        Map<String, Set<String>> newSocialNetwork = new HashMap<>();
        Map<String, Set<String>> basicMapLowercase = HelperMethods.convertMapLowercase(basicMap);
        for (String author: basicMapLowercase.keySet()){//for each author, get the people that the author follows
            Map<String, Integer> secondInfluencersChildrenTally = new HashMap<>(); //for storing the number of firstInfluencers that each secondInfluencers has as followers, per author
            Set<String> firstInfluencers = new HashSet<String>(new ArrayList<String>(basicMapLowercase.get(author))); //deep copy of Set basicMap.get(author)
            newSocialNetwork.put(author, firstInfluencers); //copy these people to the new social network
            for (String firstInfluence: basicMapLowercase.get(author)){//for each person that author follows in the old social network, get the people that they follow
                Set<String> secondInfluencers = basicMapLowercase.get(firstInfluence); 
                if (secondInfluencers == null){
                    continue;
                }
                for (String secondInfluence: secondInfluencers){//update number of firstInfluencers that each secondInfluencers has as followers
                    if (secondInfluencersChildrenTally.containsKey(secondInfluence)){
                        int newNumberFirstInfluenced = secondInfluencersChildrenTally.get(secondInfluence) + 1;
                        secondInfluencersChildrenTally.put(secondInfluence, newNumberFirstInfluenced);
                    }else{
                        secondInfluencersChildrenTally.put(secondInfluence, 1);
                    }
                    if (secondInfluencersChildrenTally.get(secondInfluence) >= thresholdFirstInfluencers){//include secondInfluencer as person author follows, if past threshold
                        newSocialNetwork.get(author).add(secondInfluence);
                    }
                }
            }
        }
        return newSocialNetwork;
    }
    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        List<String> influenceList = new ArrayList<>();
        Map<String, Integer> influenceTally = new HashMap<>(); //to keep track of how much influence each mentioned user has
        Comparator<String> influenceComparator = new Comparator<String>(){// comparator to override normal string comparison for list sorting,
            @Override public int compare(String name1, String name2) {
                return -1*influenceTally.get(name1).compareTo(influenceTally.get(name2)); //compares based on value in influenceTally, multiplied by negative 1 for descending order
            } 
        };
        for (String tweetAuthor: followsGraph.keySet()){ //go through each time a user is followed, ;keep track of times mentioned in influenceTally
            String tweetAuthorLowercase = tweetAuthor.toLowerCase();
            if (!influenceTally.containsKey(tweetAuthorLowercase)){
                influenceTally.put(tweetAuthorLowercase, 0);
            }
            for (String followedPerson: followsGraph.get(tweetAuthor)){
                String followedPersonLowercase = followedPerson.toLowerCase();
                if (influenceTally.containsKey(followedPersonLowercase)){ //update dict with new tally for the followed person (followedPerson)
                    int prevTally = influenceTally.get(followedPersonLowercase);
                    influenceTally.put(followedPersonLowercase, prevTally + 1);
                }else{
                    influenceTally.put(followedPersonLowercase, 1);
                }
            }
        }
        influenceList.addAll(influenceTally.keySet()); //populate influenceList with all followed users
        Collections.sort(influenceList, influenceComparator); //sort
        return influenceList;
    }   
}


