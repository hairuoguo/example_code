/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    //We test for labels of different types
    
    @Test
    public void testGraphIntegerSet(){
        Graph<Integer> testGraph = Graph.empty();
        testGraph.set(1, 5, 5);
        assertTrue("5 is target of vertex 1", testGraph.targets(1).containsKey(5));
        assertEquals("weight of edge from 1 to 5 is 5", 5, testGraph.targets(1).get(5).intValue());   
    }
    
    @Test
    public void testGraphDoubleSet(){
        Graph<Double> testGraph = Graph.empty();
        testGraph.set(1.0, 4.2, 5);
        assertTrue("1.0 is the source of 4.2", testGraph.sources(4.2).containsKey(1.000));
        assertEquals("weight of edge from 1.0 to 4.2 is 5", 5, testGraph.sources(4.2).get(1.0000).intValue());
    }
    
   public void testGraphCharAdd(){
       Graph<Character> testGraph = Graph.empty();
       testGraph.set('a', 'B', 10);
       assertTrue("a is the source of b", testGraph.sources('b').containsKey('a'));
       assertEquals("weight of edge from a to b is 10", 10, testGraph.sources('b').get('a').intValue());

   }
    
    // TODO test other vertex label types in Problem 3.2
    
}
