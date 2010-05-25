package edu.stanford.math.plex_plus.deprecated;

import edu.stanford.math.plex_plus.graph.AbstractGraph;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * 
 * @author Andrew Tausz
 *
 */
@Deprecated
public class BronKerboschAlgorithm {
	
	public static THashSet<TIntHashSet> getAllMaximalCliques(AbstractGraph graph) {
		THashSet<TIntHashSet> cliques = new THashSet<TIntHashSet>();
		
		TIntHashSet potentialClique = new TIntHashSet();
		TIntHashSet candidates = new TIntHashSet();
		TIntHashSet alreadyFound = new TIntHashSet();
		
		fill(candidates, 0, graph.getNumVertices());
		
		findCliques(graph, cliques, potentialClique, candidates, alreadyFound);
		
		return cliques;
	}
	
	private static void findCliques(AbstractGraph graph, THashSet<TIntHashSet> cliques, TIntHashSet potentialClique, TIntHashSet candidates, TIntHashSet alreadyFound) {
		TIntHashSet candidatesCopy = new TIntHashSet(candidates);
		
		if (!existsFullMatch(graph, candidates, alreadyFound)) {
			
			TIntIterator candidateIterator = candidatesCopy.iterator();
			
			while (candidateIterator.hasNext()) {
				int candidate = candidateIterator.next();
				TIntHashSet newCandidates = new TIntHashSet();
				TIntHashSet newAlreadyFound = new TIntHashSet();
				
				potentialClique.add(candidate);
				candidates.remove(candidate);
				
				TIntIterator newCandidateIterator = candidates.iterator();
				while (newCandidateIterator.hasNext()) {
					int newCandidate = newCandidateIterator.next();
					if (graph.containsEdge(candidate, newCandidate)) {
						newCandidates.add(newCandidate);
					}
				}
				
				TIntIterator alreadyFoundIterator = alreadyFound.iterator();
				while (alreadyFoundIterator.hasNext()) {
					int newFound = alreadyFoundIterator.next();
					if (graph.containsEdge(candidate, newFound)) {
						newAlreadyFound.add(newFound);
					}
				}
				
				if ((newCandidates.isEmpty() && newAlreadyFound.isEmpty())) {
					// potentialClique is maximal
					cliques.add(new TIntHashSet(potentialClique));
				} else {
					findCliques(graph, cliques, potentialClique, newCandidates, newAlreadyFound);
				}
				
				alreadyFound.add(candidate);
				potentialClique.remove(candidate);
			}
		}
	}
	
	/**
	 * This function returns true if a node in alreadyFound is connected to 
	 * every node in the candidates set, and false otherwise.
	 * 
	 * @param graph
	 * @param candidates
	 * @param alreadyFound
	 * @return
	 */
	private static boolean existsFullMatch(AbstractGraph graph, TIntHashSet candidates, TIntHashSet alreadyFound) {
		int edgeCounter = 0;
		
		TIntIterator foundIterator = alreadyFound.iterator();
		while (foundIterator.hasNext()) {
			int found = foundIterator.next();
			edgeCounter = 0;
			
			TIntIterator candidateIterator = candidates.iterator();
			while (candidateIterator.hasNext()) {
				int candidate = candidateIterator.next();
				if (graph.containsEdge(found, candidate)) {
					edgeCounter++;
				}
			}
			
			if (edgeCounter == candidates.size()) {
				return true;
			}
		}
		
		return false;
	}
	
	private static void fill(TIntHashSet container, int start, int end) {
		for (int i = start; i < end; i++) {
			container.add(i);
		}
	}
}
