package twitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MySocialNetworkTest {
    

    /*Testing strategy for triadicClosure
     * Input space: Social network with no listings, social network with multiple listings, social network with users following nobody
     * thresholdFirstInfluencers < 0, thresholdFirstInfluencers = 0, thresholdFirstInfluencers > 0
     * Output space: returns social network with no listings, social network with multiple listings, social network with users following nobody
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*Start tests for triadicClosure()*/
    //Test 1: Social network with no listings, returns no listings
    @Test
    public void testTriadicClosureEmptySocialNetwork(){
        Map<String, Set<String>> oldSocialNetwork = new HashMap<>();
        Map<String, Set<String>> newSocialNetwork = SocialNetwork.triadicClosure(oldSocialNetwork, 1);

        assertTrue("expected size of map is 0", newSocialNetwork.size() == 0);
        
    }
    
    //Test 2: Social network with multiple listings, some empty lists, returns some empty lists, thresholdFirstInfluenecers = 1
    @Test
    public void testTriadicClosureMultipleListingsThresholdOne(){
        Map<String, Set<String>> oldSocialNetwork = new HashMap<>();
        //@Carolyn Lu - Yes. @grader - allusion to my living group b/c of names I used
        oldSocialNetwork.put("person1", new HashSet<String>(Arrays.asList("jack_florEy", "sporT_death")));
        oldSocialNetwork.put("perSon2", new HashSet<String>(Arrays.asList("jack_florey", "kroTus")));
        oldSocialNetwork.put("person3", new HashSet<String>(Arrays.asList()));
        oldSocialNetwork.put("sport_deAth", new HashSet<String>(Arrays.asList("krOtus")));
        oldSocialNetwork.put("jaCk_florey", new HashSet<String>(Arrays.asList("krotUs")));
        Map<String, Set<String>> newSocialNetwork = SocialNetwork.triadicClosure(oldSocialNetwork, 1);
        Map<String, Set<String>> newSocialNetworkLowercase = SocialNetworkTest.convertMapLowercase(newSocialNetwork);
        assertTrue("expected size of map is 5", newSocialNetworkLowercase.size() == 5);
        assertTrue("expected person1 to be in map", newSocialNetworkLowercase.keySet().contains("person1"));
        assertTrue("expected person1 to contain krotus as influencer", newSocialNetworkLowercase.get("person1").contains("krotus"));
        assertTrue("expected person2 to be in map", newSocialNetworkLowercase.keySet().contains("person2"));
        assertTrue("expected person2 to contain jack_florey, sport_death as influencers", newSocialNetworkLowercase.get("person2").containsAll(Arrays.asList("jack_florey", "krotus")));
        assertTrue("expected person3 to be in map", newSocialNetworkLowercase.keySet().contains("person3"));
        assertTrue("expected person3 to contain no influences", newSocialNetworkLowercase.get("person3").size() == 0);
        
        

    }
    
    /*End tests for traidicClosure()*/
    
}
