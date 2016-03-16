/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>(new HashSet<String>(), new ArrayList<Edge<String>>());
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     * Strategy for testing ConcreteEdgesGraph:
     *      
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    // Input:
    //      Empty graph, Graph with no edges, graph has edges
    // We test for all these cases
    
    
    @Test
    public void testToStringEmptyGraph(){
        Graph<String> testGraph = emptyInstance();
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("There should be 2 lines", 2, lines.length);
        assertTrue("should mention that there are 0 vertices", returnedString.contains("0 vertices"));
        assertTrue("should mention that there are 0 edges", returnedString.contains("0 edges"));
    }
    
    @Test
    public void testToStringNoEdges(){
        Graph<String> testGraph = emptyInstance();
        testGraph.add("v1");
        testGraph.add("v2");
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("There should be 3 lines", 3, lines.length);
        assertTrue("should mention that there are 2 vertices", returnedString.contains("2 vertices"));
        assertTrue("should mention v1", returnedString.contains("v1"));
        assertTrue("should mentioned v2", returnedString.contains("v2"));
    }
    
    @Test
    public void testToStringHasEdges(){
        Graph<String> testGraph = emptyInstance();
        testGraph.set("v1", "v2", 5);
        testGraph.set("v2", "v3", 10);
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("There should be 5 lines", 5, lines.length);
        assertTrue("should mention that there are 3 vertices", returnedString.contains("3 vertices"));
        assertTrue("should mention that there are 2 edges", returnedString.contains("2 edges"));
        String edgeOneString = "from v1 to v2 has weight of: 5";
        String edgeTwoString = "from v2 to v3 has weight of: 10";
        assertTrue("should mention edge from v1 to v2", returnedString.contains(edgeOneString));
        assertTrue("should mention edge from v2 to v3", returnedString.contains(edgeTwoString));
        
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    // Input:
    //      Empty string, weight of 1
    // We test for all these cases
    // Testing strategy for getWeight():
    // We test retrieving the weight on a given edge
    // Testing strategy for getTo()
    // We test retrieving the target vertex of a given edge
    // Testing strategy for getFrom():
    // We test retrieving the source vertex of a given edge
    // Testing strategy for toString():
    // We test retrieving the string representations of an edge with both vertices labeled
    // and one with one edge that has its label as an empty string
    
    @Test
    public void testEdgeEmptyString(){
        Edge<String> testEdge = new Edge<String>("v1", "", 5 );
        assertEquals("target vertex should be empty string", testEdge.getTo(), "");
        assertEquals("weight of edge should be 5", testEdge.getWeight(), 5);
        assertEquals("source vertex should be v1", testEdge.getFrom(), "v1");
        
    }
    @Test
    public void testEdgeWeightOne(){
        Edge<String> testEdge = new Edge<String>("v1", "v2", 1);
        assertEquals("target vertex should be v2", testEdge.getTo(), "v2");
        assertEquals("weight of edge should be 1", testEdge.getWeight(), 1);
        assertEquals("source vertex should be v1", testEdge.getFrom(), "v1");
    }
    
    @Test
    public void testEdgeGetWeight(){
        Edge<String> testEdge = new Edge<String>("v1", "v2", 5);
        assertEquals("weight of edge should be 5", testEdge.getWeight(), 5);
    }
    
    @Test
    public void testEdgeGetTo(){
        Edge<String> testEdge = new Edge<String>("v1", "v2", 5);
        assertEquals("vertex should be v2", testEdge.getTo(), "v2");
    }
    
    @Test
    public void testEdgeGetFrom(){
        Edge<String> testEdge = new Edge<String>("v1", "v2", 5);
        assertEquals("vertex should be v1", testEdge.getFrom(), "v1");
    }
    
    @Test
    public void testEdgeToString(){
        Edge<String> testEdge = new Edge<String>("v1", "v2", 5);
        String retrievedString = testEdge.toString();
        assertTrue("String is as expected", retrievedString.equals("Edge from v1 to v2 has weight of: 5"));
    }
    
    @Test
    public void testEdgeToStringEmptyLabel(){
        Edge<String> testEdge = new Edge<String>("", "v2", 5);
        String retrievedString = testEdge.toString();
        assertTrue("String is as expected", retrievedString.equals("Edge from  to v2 has weight of: 5"));
    }
    
}
