/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    // We test each of the instance methods
    // Strategies for each method are before its tests
    
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // Testing strategy for add():
    // Inputs:
    //      Label: Empty string
    //      Graph: already has vertex, graph doesn't have vertex, graph is empty,
    // We test for each of these cases
    
    @Test
    //case where vertex name is empty string
    public void testAddVertexEmptyString(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("");
        assertEquals("number of vertices should be 1", 1, testInstance.vertices().size());
    }

    @Test 
    //case where graph already has vertex with same label
    public void testAddGraphAlreadyHasVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertFalse("should return false", testInstance.add("v1"));
        assertEquals("number of vertices should equal 1", 1, testInstance.vertices().size());
        assertTrue("still contains v1", testInstance.vertices().contains("v1"));
    }
    
    @Test
    //case where graph doesn't already have vertex with same label
    public void testAddGraphDoesntHaveVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertTrue("should return true", testInstance.add("v2"));
        assertEquals("number of vertices should equal 2", 2, testInstance.vertices().size());
        assertTrue("contains v1 and v2", testInstance.vertices().containsAll(Arrays.asList("v1", "v2")));
    }
    
    @Test
    //case where the graph is empty
    public void testAddGraphEmpty(){
        Graph<String> testInstance = emptyInstance();
        assertTrue("should return true", testInstance.add("v1"));
        assertEquals("number of vertices should equal 1", 1, testInstance.vertices().size());
        assertTrue("contains v1", testInstance.vertices().contains("v1"));
    }

    
    // Testing strategy for remove():
    // Inputs:
    //      Label: Empty string
    //      Graph: has vertex, doesn't have vertex, graph is empty, graph has one vertex, vertex has edges
    // We test for each of these cases
    
    @Test
    //case where the label of the vertex is the empty string
    public void testRemoveVertexEmptyString(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("");
        testInstance.remove("");
        assertEquals("number of vertices should be 0", 0, testInstance.vertices().size());
    }
    
    @Test
    //case where the graph has the vertex with the input label
    public void testRemoveGraphHasVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        assertTrue("should return true", testInstance.remove("v1"));
        assertEquals("number of verticies should equal 1", 1, testInstance.vertices().size());
        assertTrue("contains v2", testInstance.vertices().contains("v2"));
    }
    
    @Test
    //case where the graph doesn't have a vertex with the input label
    public void testRemoveGraphDoesntHaveVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        assertFalse("should return false", testInstance.remove("v3"));
        assertEquals("number of verticies should equal 2", 2, testInstance.vertices().size());
        assertTrue("contains v1 and v2", testInstance.vertices().containsAll(Arrays.asList("v1", "v2")));
    }
    
    @Test
    //case where the graph is empty
    public void testRemoveGraphEmpty(){
        Graph<String> testInstance = emptyInstance();
        assertFalse("should return false", testInstance.remove("v1"));
        assertEquals("expected graph to have no vertices",
                Collections.emptySet(), testInstance.vertices());
    }
    
    @Test
    //case where you remove the only vertex in a graph
    public void testRemoveGraphOneVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertTrue("should return true", testInstance.remove("v1"));
        assertEquals("expected graph to have no vertices",
                Collections.emptySet(), testInstance.vertices());
    }
    
    @Test
    //case where the vertex you're removing has edges
    public void testRemoveVertexHasEdges(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v2", 10);
        testInstance.set("v2", "v1", 5);
        assertTrue("should return true", testInstance.remove("v1"));
        assertEquals("size of v2 sources should be 0", 0, testInstance.sources("v2").size());
        assertEquals("size of v2 targets should be 0", 0, testInstance.targets("v2").size());
        assertEquals("number of vertices is 1", 1, testInstance.vertices().size());
        assertTrue("graph should contain v2", testInstance.vertices().contains("v2"));
        
    }
    
    // Testing strategy for set():
    // Inputs: 
    //      Source and target are same, graph doesn't have source, graph doesn't have target,
    //      graph already has edge, graph has edge and weight to be set is 0, graph doesn't have vertices
    //      and weight to be set is 0, empty graph, source is empty string
    // We test for each of these cases.
    
    @Test
    //case where you're setting an edge from a vertex to itself
    public void testSetCircularEdge(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v1", 10);
        assertEquals("the number of vertices is 1", testInstance.vertices().size(), 1);
        assertTrue("v1 is a target of itself", testInstance.targets("v1").containsKey("v1"));
        assertTrue("v1 is a source of itself", testInstance.sources("v1").containsKey("v1"));
        assertEquals("the weight of the edge is 10", 10, testInstance.sources("v1").get("v1").intValue());
    }
    
    @Test
    //case where the graph doesn't have the inputed source vertex
    public void testSetGraphDoesntHaveSource(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        testInstance.add("v3");
        assertEquals("the graph does not have the specified source", 0, testInstance.set("v4", "v2", 5));
        assertEquals("size of graph should be 4", testInstance.vertices().size(), 4);
        assertTrue("graph should contain v1, v2, v3, v4", testInstance.vertices().containsAll(Arrays.asList("v1", "v2", "v3", "v4")));
        assertEquals("edge weight from v4 to v2 should be 5", 5, testInstance.sources("v2").get("v4").intValue());
    }
    
    @Test
    //case where the graph doesn't have the inputed target vertex
    public void testSetGraphDoesntHaveTarget(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        testInstance.add("v3");
        assertEquals("the graph added a new edge", 0, testInstance.set("v2", "v4", 5));
        assertEquals("size of graph should be 4", testInstance.vertices().size(), 4);
        assertTrue("graph should contain v1, v2, v3, v4", testInstance.vertices().containsAll(Arrays.asList("v1", "v2", "v3", "v4")));
        assertEquals("edge weight from v2 to v4 should be 5", 5, testInstance.sources("v4").get("v2").intValue());
    }
    
    @Test
    //case where you set the weight of the edge to be zero to remove it
    public void testSetWeightZero(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        testInstance.add("v3");
        testInstance.set("v1", "v3", 5);
        assertEquals("the graph removed the edge", 5, testInstance.set("v1", "v3", 0));
        assertEquals("size of graph should be 3", testInstance.vertices().size(), 3);
        assertTrue("graph should contain v1, v2, v3", testInstance.vertices().containsAll(Arrays.asList("v1", "v2", "v3")));
        assertFalse("v1 should not be source of v3", testInstance.sources("v3").keySet().contains("v1"));
    }
    
    
    
    @Test
    //case where both vertices already exist and you change the edge weight to positive non-zero integer
    public void testSetSourceTargetInGraphWeightNonZero(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.add("v2");
        testInstance.add("v3");
        testInstance.set("v1", "v3", 5);
        assertEquals("changed weight of edge to 10", 5, testInstance.set("v1", "v3", 10));
        assertEquals("size of graph should be 3", testInstance.vertices().size(), 3);
        assertTrue("graph should contain v1, v2, v3", testInstance.vertices().containsAll(Arrays.asList("v1", "v2", "v3")));
        assertEquals("edge weight from v1 to v3 should be 10", 10, testInstance.sources("v3").get("v1").intValue());

    }
    
    @Test
    //case where inputed vertices don't exist and the inputed weight is zero
    //tests to make sure that vertices are not added
    public void testSetSourceTargetNotInGraphWeightZero(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v2", 0);
        assertEquals("graph should be empty", 0, testInstance.vertices().size());
    }
    
    @Test
    //case where graph is empty at first
    public void testSetGraphEmpty(){
        Graph<String> testInstance = emptyInstance();
        assertEquals("should return 0", 0, testInstance.set("v1", "v3", 0));
        assertEquals("expected graph to have zero vertices",
                0, testInstance.vertices().size());
    }
    
    @Test
    //case where one of vertices is labeled with the empty string
    public void testSetEmptyString(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "", 5);
        assertEquals("size of graph should be 2", testInstance.vertices().size(), 2);
        assertTrue("graph should contain v1, empty string", testInstance.vertices().containsAll(Arrays.asList("v1", "")));
        assertEquals("edge weight from v1 to empty string should be 5", 5, testInstance.sources("").get("v1").intValue());
    }
   
    // Testing strategy for sources():
    // Inputs: 
    //      empty graph, graph with one vertex, target has no sources, target has sources
    // We test for all of these cases
    //      
    
    @Test
    //case where the graph is empty
    public void testSourcesEmptyGraph(){
        Graph<String> testInstance = emptyInstance();
        assertEquals("should return empty map", 0, testInstance.sources("v1").size());
    }
    
    @Test
    //case where the graph has one vertex
    public void testSourcesGraphOneVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertEquals("should return empty map", 0, testInstance.sources("v1").size());
    }
    
    
    @Test
    //case where the input vertex has no sources
    public void testSourcesTargetNoSources(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v2", 5);
        testInstance.set("v2", "v1", 5);
        testInstance.add("v3");
        assertEquals("should return empty map", 0, testInstance.sources("v3").size());
    }
   
    
    @Test
    //case where the input vertex has sources
    public void testSourcesHasSources(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v2", 5);
        testInstance.set("v2", "v1", 5);
        testInstance.set("v3", "v2", 10);
        assertEquals("should have two sources", 2, testInstance.sources("v2").size());
        assertEquals("entry for v1 should be 5", 5, testInstance.sources("v2").get("v1").intValue());
        assertEquals("entry for v3 should be 10", 10, testInstance.sources("v2").get("v3").intValue());
    }
    
    // Testing strategy for targets():
    // Inputs:
    //      empty graph, graph with one vertex, source has no targets, source has targets
    // We test for all of these cases 
    //
    
    @Test
    //case where the graph is empty
    public void testTargetsEmptyGraph(){
        Graph<String> testInstance = emptyInstance();
        assertEquals("should return empty map", 0, testInstance.targets("v1").size());
        
    }
    
    @Test
    //case where the graph has only one vertex
    public void testTargetsOneVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertEquals("should return empty map", 0, testInstance.targets("v1").size());
    }
    
    
    @Test
    //case where the vertex has no target vertices
    public void testTargetsNoTargets(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v3", 5);
        testInstance.set("v2", "v3", 5);
        assertEquals("should return empty map", 0, testInstance.targets("v3").size());
    }
    
    @Test
    //case where the vertex has target vertices
    public void testTargetsHasTargets(){
        Graph<String> testInstance = emptyInstance();
        testInstance.set("v1", "v3", 5);
        testInstance.set("v1", "v2", 10);
        assertEquals("should return map of size 2", 2, testInstance.targets("v1").size());
        assertEquals("weight of edge to v3 should be 5", 5, testInstance.targets("v1").get("v3").intValue());
        assertEquals("weight of edge to v2 shoud be 10", 10, testInstance.targets("v1").get("v2").intValue());
    }

    // Testing strategy for vertices():
    // Inputs:
    //      empty graph, one vertex, many vertices
    // We test for all of these cases
    // 
    
    @Test
    //case where the graph is empty
    public void testVerticesEmptyGraph(){
        Graph<String> testInstance = emptyInstance();
        assertEquals("size of returned set should be 0", 0, testInstance.vertices().size());
    }
    
    @Test
    //case where the graph is non-empty
    public void testVerticesHasVertices(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        testInstance.set("v2", "v3", 10);
        assertEquals("size of returned set should be 3", 3, testInstance.vertices().size());
        assertTrue("returned set should contain v1, v2, v3", testInstance.vertices().containsAll(Arrays.asList("v1", "v2", "v3")));
    }
    
    @Test
    //case where the graph has only one vertex
    public void testVerticesOneVertex(){
        Graph<String> testInstance = emptyInstance();
        testInstance.add("v1");
        assertEquals("size of returned set should be 1", 1, testInstance.vertices().size());
        assertTrue("returned set should contain v1", testInstance.vertices().contains("v1"));
    }
}
