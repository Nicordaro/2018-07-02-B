package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private ExtFlightDelaysDAO dao;
	private List<Airport> allAirports;
	private Map<Integer, Airport> airportsIdMap;
	private Graph<Airport, DefaultWeightedEdge> graph;

	public Model() {
		this.dao = new ExtFlightDelaysDAO();
		this.allAirports = new ArrayList<Airport>(dao.loadAllAirports());
		this.airportsIdMap = new HashMap<Integer, Airport>();
		for (Airport a : this.allAirports) {
			airportsIdMap.put(a.getId(), a);
		}
		this.graph = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}

	public List<Airport> getAllAirports() {
		return allAirports;
	}

	public Map<Integer, Airport> getAirportsIdMap() {
		return airportsIdMap;
	}

	public void createGraph(int flights) {
		if (graph.vertexSet().size() != 0) {
			this.clearGraph(graph);
		}

		List<Airport> vertices = new ArrayList<Airport>();
		for (Airport a : this.allAirports) {
			int flightsDAO = dao.loadAirportsStats(a);
			if (flightsDAO > flights) {
				vertices.add(a);
			}
		}
		Graphs.addAllVertices(graph, vertices);
		for (Airport a1 : graph.vertexSet()) {
			for (Airport a2 : graph.vertexSet()) {
				if (dao.isConnected(a1, a2) && a1.getId() < a2.getId()) {
					double weight = dao.edgeWeight(a1, a2);
					Graphs.addEdgeWithVertices(graph, a1, a2, weight);
					System.out.format("%d - %d , weight %f \n", a1.getId(), a2.getId(), weight);
				}
			}
		}

	}

	public List<Airport> getNeighbours(Airport a) {
		return Graphs.neighborListOf(graph, a);
	}

	public double getWeight(Airport a1, Airport a2) {
		return graph.getEdgeWeight(graph.getEdge(a1, a2));
	}

	// SERVONO PER PULIRE IL GRAFO ALTRIMENTI IL CONNECTIVITY INSPECTOR TIENE
	// MEMORIZZATA L'ULTIMA SIZE DEL GRAFO

	public static <V, E> void removeAllEdges(Graph<V, E> graph) {
		LinkedList<E> copy = new LinkedList<E>();
		for (E e : graph.edgeSet()) {
			copy.add(e);
		}
		graph.removeAllEdges(copy);
	}

	public <V, E> void clearGraph(Graph<V, E> graph) {
		removeAllEdges(graph);
		removeAllVertices(graph);
	}

	public static <V, E> void removeAllVertices(Graph<V, E> graph) {
		LinkedList<V> copy = new LinkedList<V>();
		for (V v : graph.vertexSet()) {
			copy.add(v);
		}
		graph.removeAllVertices(copy);
	}

	public Graph<Airport, DefaultWeightedEdge> getGraph() {
		return graph;
	}

}
