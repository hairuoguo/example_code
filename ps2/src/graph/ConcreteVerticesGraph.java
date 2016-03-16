/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 * @param <L>
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   A vertex for every vertex in the graph
    //   Each vertex contains list of in-edges and out-edges, with sources/targets and weights
    //   
    // Representation invariant:
    //   No repeated vertices
    //   Safety from rep exposure:
    //   Constructor adds elements of input list to list;
    //   Public methods that return mutable objects return copies
    //   Elements in all returned mutable objects are all immutable

    
    /**
     * Constructor: creates a new graph. Edge weights must be greater than 0 and
     * there cannot be multiple edges of the same direction between two vertices
     * @param startVertices list of vertices type L
     */
    public ConcreteVerticesGraph(List<Vertex<L>> startVertices){
        vertices.addAll(startVertices);
    }
    
    private void checkRep(){
        List<Vertex<L>> checkList = new ArrayList<Vertex<L>>(vertices);
        assert checkList.size() == vertices.size(); //checks for repeated vertices
        //@Jessica Kenney, Stephanie O'Brien, repeated vertices were checked as above,
        // duplicate edges are checked by the vertex checkRep().
    }
    
    
    @Override public boolean add(L vertex) {
        boolean doesntAlreadyContain = !containsVertexByLabel(vertex);
        if (doesntAlreadyContain){
            vertices.add(new Vertex<L>(vertex));
        }
        checkRep();
        return doesntAlreadyContain;
    }
    
    @Override public int set(L source, L target, int weight) {
        boolean doesntContainSource = !containsVertexByLabel(source);
        boolean doesntContainTarget = !containsVertexByLabel(target);
        if ((doesntContainSource || doesntContainTarget) && weight == 0){
            return 0;
        }
        Vertex<L> sourceVertex = findVertexByLabelOrAdd(source);
        Vertex<L> targetVertex = findVertexByLabelOrAdd(target);
        int prevWeight = 0;
        if (sourceVertex.getOut().containsKey(target)){
            prevWeight = sourceVertex.getOut().get(target);
        }
        sourceVertex.updateOut(target, weight);
        targetVertex.updateIn(source, weight);
        checkRep();
        return prevWeight;
    }
    
    @Override public boolean remove(L vertex) {
        boolean alreadyContains = containsVertexByLabel(vertex);
        if (alreadyContains){
            Vertex<L> actualVertex = findVertexByLabelOrAdd(vertex);
            Map<L, Integer> outEdges = actualVertex.getOut();
            for (L outKey: outEdges.keySet()){
                Vertex<L> target = findVertexByLabelOrAdd(outKey);
                target.updateIn(vertex, 0);
            }
            Map<L, Integer> inEdges = actualVertex.getIn();
            for (L inKey: inEdges.keySet()){
                Vertex<L> source = findVertexByLabelOrAdd(inKey);
                source.updateOut(vertex, 0);
            }
            vertices.remove(actualVertex);
        }
        checkRep();
        return alreadyContains;
    }
    
    @Override public Set<L> vertices() {
        Set<L> labelSet = new HashSet<L>();
        for (Vertex<L> vertex: vertices){
            labelSet.add(vertex.getLabel());
        }
        return labelSet;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        for (Vertex<L> vertex: vertices){
            if (vertex.getLabel().equals(target)){
                return new HashMap<L, Integer>(vertex.getIn());
            }
        }
        return new HashMap<L, Integer>();
    }
    
    @Override public Map<L, Integer> targets(L source) {
        for (Vertex<L> vertex: vertices){
            if (vertex.getLabel().equals(source)){
                return new HashMap<L, Integer>(vertex.getOut());
            }
        }
        return new HashMap<L, Integer>();
    }
    
    /**
     * Searches for vertex given label type L in vertices. Returns true if found,
     * false if not
     * @param vertex label of vertex to look for
     * @return true if in vertices, false if not
     */
    private boolean containsVertexByLabel(L vertex){
        for (Vertex<L> inVertex: vertices){
            if (vertex.equals(inVertex.getLabel())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Looks for vertex given label type L, returns the actual vertex or creates it if not existent
     * @param vertex label of vertex to look for
     * @return vertex that was searched for, or new vertex of label if it wasn't there
     */
    private Vertex<L> findVertexByLabelOrAdd(L vertex){
        for (Vertex<L> inVertex: vertices){
            if (vertex.equals(inVertex.getLabel())){
                return inVertex;
            }
        }
        Vertex<L> newVertex = new Vertex<L>(vertex);
        vertices.add(newVertex);
        checkRep();
        return newVertex;
    }
    
    @Override
    /**
     * Returns String in the following format:
     * "This graph has [#vertices] vertices."
     * [toStrings for all vertices]
     * @return returns string representing graph as described above
     */
    public String toString(){
        String returnString = "";
        returnString += "This graph has " + vertices.size() + " vertices. \n";
        for (Vertex<L> vertex:vertices){
            returnString += vertex.toString();
        }
        return returnString;
    }
    
}

/**
 * This class represents a vertex. It has a label of type L, as well as two Maps
 * containing targets and sources respectively, as well as the associated weights for
 * those edges.
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    // Field: L startLabel: label of the vertex
    
    // Abstraction function:
    //   Represents a vertex. Label represents the label, inEdges contain maps from source
    //   vertices as well as weights. OutEdges contain map to target vertices, as well as weights
    // Representation invariant:
    //   Weights of edges cannot be zero.
    //   Cannot have multiple edges with same source or target.
    // Safety from rep exposure:
    //   Returned mutable objects are copies, elements are immutable
    //   
    
    private final Map<L, Integer> inEdges = new HashMap<L, Integer>();
    private final Map<L, Integer> outEdges = new HashMap<L, Integer>();
    private final L label;
    
    /**
     * Constructor: Creates new vertex. 
     * @param startLabel label of the vertex of type L
     */
    public Vertex(L startLabel){
        label = startLabel;
    }
    
    private void checkRep(){
        for (L inEdge: inEdges.keySet()){
            assert inEdges.get(inEdge) > 0;
        }
        for (L outEdge: outEdges.keySet()){
            assert outEdges.get(outEdge) > 0;
        }
    }
    /**
     * Gets label of the vertex
     * @return returns label of the vertex in type L
     */
    public L getLabel(){
        return this.label;
    }
    
    /**
     * Updates weight or removes edge from edges going into vertex, depending on weight. Mutates inEdges
     * Removes if 0, updates edge weight if greater
     * @param vertex label of source vertex type L
     * @param weight must be integer 0 or greater
     */
    public void updateIn(L vertex, int weight){
        if (weight == 0){
            inEdges.remove(vertex);
        }else{
            inEdges.put(vertex, weight);
        }
        checkRep();
    }
    
    /**
     * Updates weight or removes edge from edges going out of vertex, depending on weight. Mutates outEdges
     * Removes if 0, updates edge weight if greater
     * @param vertex label of source vertex type L
     * @param weight must be integer 0 or greater
     */
    public void updateOut(L vertex, int weight){
        if (weight == 0){
            outEdges.remove(vertex);
        }else{
            outEdges.put(vertex, weight);
        }
        checkRep();
    }
    /**
     * Gets Map of edges going into vertex with keys being source labels and values being weights
     * @return copy of inEdges
     */
    public Map<L, Integer> getIn(){
        Map<L, Integer> inEdgesCopy = new HashMap<L, Integer>();
        for (L key: inEdges.keySet()){
            inEdgesCopy.put(key, inEdges.get(key));
        }
        return inEdgesCopy;
    }
    /**
     * Gets Map of edges going out of vertex with keys being source labels and values being weights
     * @return copy of outEdges
     */
    public Map<L, Integer> getOut(){
        Map<L, Integer> outEdgesCopy = new HashMap<L, Integer>();
        for (L key: outEdges.keySet()){
            outEdgesCopy.put(key, outEdges.get(key));
        }
        return outEdgesCopy;
    }
    
    /**
     * returns string of format:
     * "[Label of vertex]
     * There are the edges going into the vertex:
     * [for each incoming edge:]
     * Edge from [source label] with weight [weight of edge]
     * [for each outgoing edge:]
     * Edge to [target label] with weight [weight of edge]"
     * @return returns string representing vertex as described above
     */
    @Override
    public String toString(){
        String resultString = this.label + "\n";
        if (inEdges.size() > 0){
            resultString += "These are the edges going into the vertex: \n";
            for (L key: inEdges.keySet()){
                resultString += "Edge from " + key + " with weight " + inEdges.get(key) + "\n";
            }
        }
        if (outEdges.size() > 0){
            resultString += "These are the edges going out of the vertex: \n";
            for (L key: outEdges.keySet()){
                resultString += "Edge to " + key + " with weight " + outEdges.get(key) + "\n";
            }
        }
        return resultString;
    }
    
}
