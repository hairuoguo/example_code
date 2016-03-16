package twitter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HelperMethods{
    
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
            newSocialNetwork.put(key.toLowerCase(), convertSetElementsToLowercase(socialNetwork.get(key)));
        }
        return newSocialNetwork;
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
}
