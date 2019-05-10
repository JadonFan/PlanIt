package common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javafx.util.Pair;

public class Graph<T> {	
	protected static class Vertex<T> implements Comparable<Vertex<T>> {
		private int id;
		private T element;
		private boolean isCompleted;
		@SuppressWarnings("unused")
		private Map<Vertex<T>, Double> adjacentVertices;

		
		public Vertex(int id, T element, boolean isCompleted, Map<Vertex<T>, Double> adjacentVertices) {
			this.id = id;
			this.element = element;
			this.setIsCompleted(isCompleted);
			this.adjacentVertices = adjacentVertices;
		}
		
		
		public int getId() {
			return this.id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public T getElement() {
			return this.element;
		}

		public void setElement(T element) {
			this.element = element;
		}

		public boolean getIsCompleted() {
			return this.isCompleted;
		}

		public void setIsCompleted(boolean isCompleted) {
			this.isCompleted = isCompleted;
		}
		
		public Map<Vertex<T>, Double> getAdjacentVertices() {
			return this.getAdjacentVertices();
		}

		public void setAdjacentVertices(Map<Vertex<T>, Double> adjacentVertices) {
			this.adjacentVertices = adjacentVertices;
		}


		@Override
		public int compareTo(Vertex<T> other) {
			int comparison;
			if (this.id > other.id) {
				comparison = 1;
			} else if (this.id == other.id) {
				comparison = 0;
			} else {
				comparison = -1;
			}
			
			return comparison;
		}
	}
	
	
	private Set<Vertex<T>> adjacencyList;
	
	
	public Graph() {
		this.adjacencyList = new HashSet<>();
	}
	
	
	public Set<Vertex<T>> getAdjacencyList() {
		return this.adjacencyList;
	}
	
	
	public Vertex<T> getVertexById(final int vertexId) {
		Vertex<T> targetVertex = null;
		
		for (Vertex<T> currentVertex : this.adjacencyList) {
			if (currentVertex.getId() == vertexId) {
				targetVertex = currentVertex;
				break;
			}
		}
		
		return targetVertex;
	}
	
	
	public void addVertex(final Vertex<T> vertexToAdd, final boolean isBidirectional) {		
		// Check that other vertices point to this vertex if the vertex is intended
		// to have bidirectional edges
		if (isBidirectional) {
			for (Vertex<T> currentVertex : this.adjacencyList) {
				Double distance = vertexToAdd.getAdjacentVertices().getOrDefault(currentVertex, null);
				if (distance != null) {
					currentVertex.getAdjacentVertices().put(vertexToAdd, distance);
				}
			}
		}
		
		// Reduce the number of iterations in the previous for-each loop by one when
		// this statement is called after that loop
		this.adjacencyList.add(vertexToAdd); 
	}

	
	public void deleteVertex(final int vertexIdToRemove) {
		this.deleteVertex(vertexIdToRemove, this.adjacencyList);
	}
	
	public void deleteVertex(final Vertex<T> vertexToRemove) {
		this.deleteVertex(vertexToRemove.getId());
	}
	
 	protected void deleteVertex(final int vertexIdToRemove, Set<Vertex<T>> adjacencyList) {
 		Iterator<Vertex<T>> it = adjacencyList.iterator();
 		
 		while (it.hasNext()) {
 			Vertex<T> currentVertex = it.next();
 		    
 		    if (currentVertex.getId() == vertexIdToRemove) {
 		    	it.remove();
 		    } else {
 		    	currentVertex.getAdjacentVertices().entrySet().removeIf(entry -> entry.getKey().getId() == vertexIdToRemove);
 		    }
 		}
	}
	
	
	public void sortGraph() {	
		Set<Vertex<T>> sortedAdjList = new HashSet<>();
		final int verticesCount = this.adjacencyList.size();
		
		@SuppressWarnings("unchecked")
		Vertex<T>[] vertices = this.adjacencyList.toArray((Vertex<T>[]) new Vertex[verticesCount]);
		Arrays.sort(vertices);
		
		// Given that the list of vertices is sorted at this point, we'll now sort the
		// adjacency list based on the sorted list of vertices
		for (Vertex<T> vertex : vertices) {
			sortedAdjList.add(vertex);
		}
		
		this.adjacencyList = sortedAdjList;
	}
	
	
	protected int getCloseVertexId(double[] dists, boolean[] isChecked) {
		double minDistance = Double.MAX_VALUE;
		int targetVertexId = -1;
		
		for (int i = 0; i < this.adjacencyList.size(); i++) {
			if (!isChecked[i] && dists[i] <= minDistance) {
				minDistance = dists[i];
				targetVertexId = i;
			}
		}
		
		return targetVertexId;
	}
	
	
	public Pair<Graph<T>, double[]> getShortestPath(Vector<Integer> ignoredVertexIds) {
		Set<Vertex<T>> tempAdjacencyList = this.adjacencyList;
		for (int ignoredVertexId : ignoredVertexIds) {
			this.deleteVertex(ignoredVertexId, tempAdjacencyList);
		}
		
		Pair<Graph<T>, double[]> shortestPathDetail = this.getShortestPath();
		this.adjacencyList = tempAdjacencyList;
		 
		return shortestPathDetail;
	}
	
	
	public Pair<Graph<T>, double[]> getShortestPath() {
		final int verticesCount = this.adjacencyList.size();
		
		double[] dists = new double[verticesCount];
		boolean[] isChecked = new boolean[verticesCount];
		Graph<T> shortestGraph = new Graph<>();
		
		for (int i = 0; i < verticesCount; i++) {
			dists[i] = Integer.MAX_VALUE;
			isChecked[i] = false;
		}
		
		Iterator<Vertex<T>> it = this.adjacencyList.iterator();
		int vCount = 0;
		
		while (it.hasNext()) {
			int closeVertexId = this.getCloseVertexId(dists, isChecked);
			isChecked[vCount] = true;
			
			int adjCount = 0;
			Map<Vertex<T>, Double> currAdjVertices = it.next().getAdjacentVertices();
			
			for (Map.Entry<Vertex<T>, Double> entry : currAdjVertices.entrySet()) {
				if (!entry.getKey().getIsCompleted() && !isChecked[adjCount] && entry.getValue() != 0 && 
						dists[closeVertexId] != Integer.MAX_VALUE && dists[closeVertexId] + entry.getValue() < dists[adjCount]) {
					dists[adjCount] = dists[closeVertexId] + entry.getValue();
					shortestGraph.addVertex(entry.getKey(), true);
				}
				
				adjCount++;
			}
			
			vCount++;
		}
		
		return new Pair<>(shortestGraph, dists);
	}
}
