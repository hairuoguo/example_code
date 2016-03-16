/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>(new ArrayList<Vertex<String>>());
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //      Inputs: case of empty graph, case where graph has no edges, case where
    //          graph has edges
    //      We test for each of these cases.
    
    @Test
    public void testToStringEmptyGraph(){
        Graph<String> testGraph = emptyInstance();
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("there should be one line", 1, lines.length);
        assertTrue("should mention that there are 0 vertices", returnedString.contains("0 vertices")); 
    }
    
    @Test
    public void testToStringNoEdges(){
        Graph<String> testGraph = emptyInstance();
        testGraph.add("v1");
        testGraph.add("v2");
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("there should be 3 lines", 3, lines.length);
        assertTrue("should mention that there are two vertices", returnedString.contains("2 vertices"));
        assertTrue("should mention v1", returnedString.contains("v1"));
        assertTrue("should mention v2", returnedString.contains("v2"));
    }
    
    @Test
    public void testToStringHasEdges(){
        Graph<String> testGraph = emptyInstance();
        testGraph.set("v1", "v2", 5);
        testGraph.set("v2", "v3", 10);
        String returnedString = testGraph.toString();
        String[] lines = returnedString.split("\n");
        assertEquals("there should be 12 lines", 12, lines.length);
        assertTrue("mentions that there are 3 vertices", returnedString.contains("has 3 vertices"));
        assertTrue("mentions edge to v2", returnedString.contains("to v2 with weight 5"));
        assertTrue("mentions edge from v1", returnedString.contains("from v1 with weight 5"));
        assertTrue("mentions edge to v3", returnedString.contains("to v3 with weight 10"));
        assertTrue("mentions edge from v2", returnedString.contains("from v2 with weight 10"));

    }
    
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //@Jingyu Li I list the cases because the input spaces are binary (weight 0 
    // or > 0, pre-existent or not). Testing for each of these cases is therefore
    // the same as testing across all the partitions.
    //
    //      Inputs:
    //          We test that the vertex can handle an empty string as label
    //      We test this case
    
    //Testing strategy for updateIn():
    //      Inputs:
    //          weight positive, edge not pre-existent, edge pre-existent, weight zero
    //      We test each of these cases
    //Testing strategy for updateOut():
    //      Inputs:
    //          weight positive, edge not pre-existent, edge pre-existent, weight zero
    //      We test each of these cases
    //Testing strategy for getIn():
    //@Angie Boggust I chose not to because my tests already cover the different cases within
    // each test (edge removed, edge weight changed, edge weight unchanged) by testing
    // on multiple edges.
    //      We test the case where the vertex has many edges that have been modified
    //Testing strategy for getOut():
    //      We test the case where the vertex has many edges that have been modified
    //Testing strategy for toString():
    //      We test the case where the vertex has many edges that have been modified
    

    
    @Test
    public void testVertexEmptyLabel(){
        Vertex<String> testVertex = new Vertex<String>("");
        assertEquals("vertex label should be empty string", testVertex.getLabel(), "");
    }
    
    @Test
    public void testVertexUpdateInAddEdge(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateIn("v2", 5);
        assertTrue("map of in edges contains v2", testVertex.getIn().containsKey("v2"));
        assertEquals("weight of edge is 5", 5, testVertex.getIn().get("v2").intValue());
    }
    
    @Test
    public void testVertextUpdateInWeightChange(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateIn("v2", 5);
        testVertex.updateIn("v2", 10);
        assertTrue("map of in edges contains v2", testVertex.getIn().containsKey("v2"));
        assertEquals("weight of edge is 10", 10, testVertex.getIn().get("v2").intValue());
        
    }
    
    @Test
    public void testVertexUpdateInRemove(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateIn("v3", 10);
        testVertex.updateIn("v2", 5);
        testVertex.updateIn("v2", 0);
        assertFalse("map of in edges does not contain v2", testVertex.getIn().containsKey("v2"));
    }
    @Test
    public void testVertexUpdateOutAddEdge(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateIn("v2", 5);
        assertTrue("map of in edges contains v2", testVertex.getIn().containsKey("v2"));
        assertEquals("weight of edge is 5", 5, testVertex.getIn().get("v2").intValue());
    }
    
    @Test
    public void testVertexUpdateOutWeightChange(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateOut("v2", 5);
        testVertex.updateOut("v2", 10);
        assertTrue("map of in edges contains v2", testVertex.getOut().containsKey("v2"));
        assertEquals("weight of edge is 10", 10, testVertex.getOut().get("v2").intValue());
    }
    
    
    @Test
    public void testVertexUpdateOutRemove(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateOut("v3", 10);
        testVertex.updateOut("v2", 5);
        testVertex.updateOut("v2", 0);
        assertFalse("map of in edges does not contain v2", testVertex.getOut().containsKey("v2"));
    }
    
    @Test
    public void testVertexGetIn(){
        Vertex<String> testVertex = new Vertex<String>("v4");
        testVertex.updateIn("v1", 5);
        testVertex.updateIn("v1", 0);
        testVertex.updateIn("v2", 5);
        testVertex.updateIn("v2", 10);
        testVertex.updateIn("v3", 10);
        assertFalse("map of in edges does not contain v1", testVertex.getIn().containsKey("v1"));
        assertTrue("map of in edges contains v2", testVertex.getIn().containsKey("v2"));
        assertTrue("map of in edges contains v3", testVertex.getIn().containsKey("v3"));
        assertEquals("value of v2 is 10", 10, testVertex.getIn().get("v2").intValue());
        assertEquals("value of v3 is 10", 10, testVertex.getIn().get("v3").intValue());
    }
    
    @Test
    public void testVertexGetOut(){
        Vertex<String> testVertex = new Vertex<String>("v4");
        testVertex.updateOut("v1", 5);
        testVertex.updateOut("v1", 0);
        testVertex.updateOut("v2", 5);
        testVertex.updateOut("v2", 10);
        testVertex.updateOut("v3", 10);
        assertFalse("map of in edges does not contain v1", testVertex.getOut().containsKey("v1"));
        assertTrue("map of in edges contains v2", testVertex.getOut().containsKey("v2"));
        assertTrue("map of in edges contains v3", testVertex.getOut().containsKey("v3"));
        assertEquals("value of v2 is 10", 10, testVertex.getOut().get("v2").intValue());
        assertEquals("value of v3 is 10", 10, testVertex.getOut().get("v3").intValue());
    }
    
    @Test
    public void testVertexToString(){
        Vertex<String> testVertex = new Vertex<String>("v1");
        testVertex.updateOut("v2", 5);
        testVertex.updateIn("v3", 10);
        String retrievedString = testVertex.toString();
        String outString = "v2 with weight 5";
        String inString = "v3 with weight 10";
        boolean containsOutString = retrievedString.contains(outString);
        boolean containsInString = retrievedString.contains(inString);
        assertTrue("returned string contains both expected strings", containsOutString && containsInString);
        
    }
}
