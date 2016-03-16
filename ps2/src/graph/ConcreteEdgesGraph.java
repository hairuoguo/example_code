/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 * @param <L>
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Each edge in edges represents a directed edge
    //   Each vertex in vertices represents a vertex
    
    // Representation invariant:
    //   No multiple edges between same vertices
    //   No zero-weight edges
    //   No edges that have nonexistent vertices
    
    // Safety from rep exposure:
    //   All fields are private
    //   Constructor adds elements from inputed collections. Element types (L, Edge<L>) are immutable
    
    /**
     * Makes a new ConcreteEdgesGraph. Weight of all entered edges must be > 0 and 
     * there cannot be multiple edges of the same direction between two vertices.
     * @param startVertices Set of vertices of label type L
     * @param startEdges List of Edges with label type L
     */
    public ConcreteEdgesGraph(Set<L> startVertices, List<Edge<L>> startEdges){
        vertices.addAll(startVertices);
        edges.addAll(startEdges);
        checkRep();
    }
    
    
    private void checkRep(){
        for (Edge<L> edge:edges){
            assert edge.getWeight() > 0;
            assert vertices.contains(edge.getFrom());
            assert vertices.contains(edge.getTo());
            for (Edge<L> alsoEdges:edges){// checking against all other edges
                if (alsoEdges != edge){
                    boolean fromDifferent = !edge.getFrom().equals(alsoEdges.getFrom());
                    boolean toDifferent = !edge.getTo().equals(alsoEdges.getTo());
                    assert (fromDifferent || toDifferent);
                }
            }
        }
    }
    
    
    @Override public boolean add(L vertex) {
        return vertices.add(vertex);
    }
    
    
    @Override public int set(L source, L target, int weight) {
        int oldWeight = 0;
        Iterator<Edge<L>> edgesItor = edges.iterator();
        while (edgesItor.hasNext()){ //removes edge if already there
            Edge<L> edge = edgesItor.next();
            if (edge.getFrom().equals(source) && edge.getTo().equals(target)){
                oldWeight = edge.getWeight();
                edgesItor.remove();
            }
        }
        if (weight > 0){ //adds new edge if weight > 0
            vertices.add(source);
            vertices.add(target);
            Edge<L> newEdge = new Edge<L>(source, target, weight);
            edges.add(newEdge);
        }
        checkRep();
        return oldWeight;
    }
    
    @Override public boolean remove(L vertex) {
        Iterator<Edge<L>> edgesItor = edges.iterator();
        while (edgesItor.hasNext()){ //removes all edges involving this vertex
            Edge<L> edge = edgesItor.next();
            if (edge.getFrom().equals(vertex) || edge.getTo().equals(vertex)){
                edgesItor.remove();
            }
        }
        checkRep();
        return vertices.remove(vertex);
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<L>(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sourcesMap = new HashMap<L, Integer>();
        for (Edge<L> edge:edges){
            if (edge.getTo().equals(target)){
                sourcesMap.put(edge.getFrom(), new Integer(edge.getWeight()));
            }
        }
        checkRep();
        return sourcesMap;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targetsMap = new HashMap<L, Integer>();
        for (Edge<L> edge:edges){
            if (edge.getFrom().equals(source)){
                targetsMap.put(edge.getTo(), new Integer(edge.getWeight()));
            }
        }
        checkRep();
        return targetsMap;
    }
    /**
     * Returns string of format:
     * "This graph has [#edges] edges."
     *  [toString for each edge]
     * "This graph has [#vertices] vertices."
     *  [toString for List of vertices] if the List is nonempty
     *  @return returns string in the above format
     */
    @Override public String toString(){
        String edgesList = "";
        edgesList += "This graph has " + edges.size() + " edges. \n";
        for (Edge<L> edge:edges){
            edgesList += edge.toString() + "\n";
        }
        String verticesList = "";
        verticesList += "This graph has " + vertices.size() + " vertices. \n";
        if (vertices.size() > 0){
            verticesList += vertices.toString();
        }
        return edgesList + verticesList;
    }

    
}

/**
 * Represents directed edge between two vertices. Constructor takes in the source vertex, target vertex,
 * and weight of edge. Weight of edge must be a positive non-zero integer.
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    // Fields:
    // L fromVertex: label of source vertex for edge
    // L toVertex: label of target vertex for edge
    // int edgeWeight: weight of edge
    
    // Abstraction function:
    //      Represents directed edge of positive nonzero weight. 
    //      fromVertex indicates source
    //      toVErtex indicates target
    //      edgeWeight indicates weight of edge
    // Representation invariant:
    //      Weight of edge is positive and nonzero
    // Safety from rep exposure:
    //      All methods return immutable values
    //      No methods change values besides constructor
    
    private L fromVertex;
    private L toVertex;
    private int edgeWeight;
    
    /**
     * Makes a new edge. Weight of edge must be greater than 0
     * @param startFrom Label of source vertex
     * @param startTo Label of target vertex
     * @param startWeight Weight of edge, must be greater than 0;
     */
    public Edge(L startFrom, L startTo, int startWeight){
        fromVertex = startFrom;
        toVertex = startTo;
        edgeWeight = startWeight;
        checkRep();
    }
    
    private void checkRep(){
        boolean edgeWeightNonZero = edgeWeight > 0;
        assert edgeWeightNonZero;
    }
    
    /**
     * Gets the weight of the edge
     * @return returns the integer weight of the edge
     */
    public int getWeight(){
        return this.edgeWeight;
    }
   /**
    * Gets the label of the source vertex of the edge
    * @return returns the label of the source vertex of the edge
    */
    public L getFrom(){
        return this.fromVertex;
    }
    /**
     * Gets the label of the target vertex of the edge
     * @return returns the label of the target vertex of the edge
     */
    public L getTo(){
        return this.toVertex;
    }
    
    @Override
    /**
     * returns string in the form of "Edge from [label for source vertex] to [label for target vertex] has weight [weight]"
     * @return returns string representing edge in above format
     */
    public String toString(){
        return "Edge from " + getFrom() + " to " + getTo() + " has weight of: " + getWeight();
    }
    
}
