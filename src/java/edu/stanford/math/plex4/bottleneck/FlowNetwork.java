package edu.stanford.math.plex4.bottleneck;

import edu.stanford.math.primitivelib.autogen.pair.ObjectIntPair;
import gnu.trove.THashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlowNetwork {
	TIntObjectHashMap<Set<FlowEdge>> eminatingEdges = new TIntObjectHashMap<Set<FlowEdge>>();
	
	TObjectIntHashMap<FlowEdge> flowValues = new TObjectIntHashMap<FlowEdge>();
	
	public void addEdge(int source, int destination, int weight) {
		FlowEdge forwardEdge = new FlowEdge(source, destination, weight);
		if (!this.eminatingEdges.containsKey(source)) {
			this.eminatingEdges.put(source, new THashSet<FlowEdge>());
		}
		
		this.eminatingEdges.get(source).add(forwardEdge);
		
		FlowEdge backwardEdge = new FlowEdge(destination, source, 0);
		if (!this.eminatingEdges.containsKey(destination)) {
			this.eminatingEdges.put(destination, new THashSet<FlowEdge>());
		}
		
		this.eminatingEdges.get(destination).add(backwardEdge);
		
		this.flowValues.put(forwardEdge, 0);
		this.flowValues.put(backwardEdge, 0);
		
		forwardEdge.setReverseEdge(backwardEdge);
		backwardEdge.setReverseEdge(forwardEdge);
	}
	
	protected Set<FlowEdge> getEdges(int source) {
		return this.eminatingEdges.get(source);
	}
	
	protected int getFlow(FlowEdge edge) {
		return this.flowValues.get(edge);
	}
	
	protected void incrementFlow(FlowEdge edge, int increment) {
		if (!this.flowValues.containsKey(edge)) {
			this.flowValues.put(edge, increment);
			return;
		}
		
		this.flowValues.put(edge, this.flowValues.get(edge) + increment);
	}
	
	public int maxFlow(int source, int sink) {
		List<ObjectIntPair<FlowEdge>> path = this.findPath(source, sink, new ArrayList<ObjectIntPair<FlowEdge>>());
		while (path != null && !path.isEmpty()) {
			int flow = this.minValue(path);
			for (ObjectIntPair<FlowEdge> pair: path) {
				this.incrementFlow(pair.getFirst(), flow);
				this.incrementFlow(pair.getFirst().getReverseEdge(), -flow);
			}
			
			path = this.findPath(source, sink, new ArrayList<ObjectIntPair<FlowEdge>>());
		}
		
		int sum = 0;
		for (FlowEdge edge: this.getEdges(source)) {
			sum += this.getFlow(edge);
		}
		
		return sum;
	}
	
	protected List<ObjectIntPair<FlowEdge>> findPath(int source, int sink, List<ObjectIntPair<FlowEdge>> path) {
		if (source == sink) {
			return path;
		}
		
		for (FlowEdge edge: this.getEdges(source)) {
			int residual = edge.getCapacity() - this.getFlow(edge);
			ObjectIntPair<FlowEdge> pair = new ObjectIntPair<FlowEdge>(edge, residual);
			if (residual > 0 && !this.contains(path, pair)) {
				path.add(pair);
				List<ObjectIntPair<FlowEdge>> result = this.findPath(edge.getDest(), sink, path);
				if (result != null && !result.isEmpty()) {
					return result;
				}
				path.remove(path.size() - 1);
			}
		}
		
		return null;
	}
	
	protected <T> int minValue(List<ObjectIntPair<T>> list) {
		int index = 0;
		int minValue = 0;
		for (ObjectIntPair<T> element: list) {
			if (index == 0 || element.getSecond() < minValue) {
				minValue = element.getSecond();
			}
			index++;
		}
		return minValue;
	}
	
	protected <T> boolean contains(Iterable<T> collection, T edge) {
		for (T t: collection) {
			if (t.equals(edge)) {
				return true;
			}
		}
		
		return false;
	}
}
