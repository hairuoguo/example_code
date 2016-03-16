/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   Vertices are words as defined in the class spec
    //   Edges are weights of how often the target vertex has appeared after the source vertex
    // Representation invariant:
    //   All labels in the graph must be lowercase.
    // Safety from rep exposure:
    //   Only returns immutable strings
    //   Only takes in immutable objects
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(corpus));
        String newLine;
        String corpusString = "";
        while ((newLine = reader.readLine()) != null){
            corpusString += " " + newLine;
        }
        reader.close();
        corpusString = corpusString.trim();
        processCorpus(corpusString);
    }
    
    private void checkRep(){
        Set<String> vertices = graph.vertices();
        for (String vertex: vertices){
            assert vertex.equals(vertex.toLowerCase());
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    
    public String poem(String input) {
        input = input.trim();
        String[] poemArray = input.split("\\s+");
        String newPoem = "";
        if (poemArray.length < 1){
            return "";
        }
        for (int i = 0; i < poemArray.length - 1; i++){
            String currentWord = poemArray[i];
            String nextWord = poemArray[i+1];
            newPoem += currentWord + " ";
            String newWord = findWordToInsert(currentWord, nextWord);
            if (!newWord.equals("")){
                newPoem += newWord + " ";
            }
        }
        newPoem += poemArray[poemArray.length - 1];
        return newPoem;
    }
    
    /**
     * Takes in corpus, mutates graph to represent corpus
     * @param corpus a string representing the corpus
     */
    private void processCorpus(String corpus){
        String[] wordsArray = corpus.toLowerCase().split("\\s+");
        for (int i = 0; i < wordsArray.length - 1; i++){
            String word = wordsArray[i];
            String nextWord = wordsArray[i+1];
            if (graph.targets(word).containsKey(nextWord)){
                int currentWeight = graph.targets(word).get(nextWord);
                graph.set(word, nextWord, currentWeight + 1);
            }else{
                graph.set(word, nextWord, 1);
            }
        }
        checkRep();
    }
    /**
     * Given two words, finds if there exists a bridge word as defined in class spec
     * Returns bridge word if it exists, otherwise an empty string
     * @param currentWord string representing currentWord in poem
     * @param nextWord string representing next word in poem
     * @return word if found, otherwise empty string
     */
    private String findWordToInsert(String currentWord, String nextWord){// goes through graph, finds word to insert
        String foundWord = "";
        currentWord = currentWord.toLowerCase();
        nextWord = nextWord.toLowerCase();
        Map<String, Integer> currentTargets = graph.targets(currentWord);
        Map<String, Integer> nextSources = graph.sources(nextWord);
        Set<String> intermediaries = new HashSet<String>(currentTargets.keySet());
        intermediaries.retainAll(nextSources.keySet());
        int adjacencyCounter = 0;
        for (String intermediary: intermediaries){
            int firstEdgeWeight = graph.targets(currentWord).get(intermediary).intValue();
            int secondEdgeWeight = graph.sources(nextWord).get(intermediary).intValue();
            int totalWeight = firstEdgeWeight + secondEdgeWeight;
            if (totalWeight > adjacencyCounter){
                adjacencyCounter = totalWeight;
                foundWord = intermediary;
            }
        }
        return foundWord;
    }
    
    /**
     * Returns string in format:
     * "This below is the adjacency graph: 
     * [representation of graph based on its toString()]"
     * @return returns string representing the corpus graph as described above.
     */
    public String toString(){
        String returnedString = "This below is the adjacency graph: \n";
        returnedString += graph.toString();
        return returnedString;
    }
    
}
