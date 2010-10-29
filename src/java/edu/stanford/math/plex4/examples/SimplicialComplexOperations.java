package edu.stanford.math.plex4.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;

public class SimplicialComplexOperations {
	ExplicitStream<Simplex> triangularSubivision(ExplicitStream<Simplex> stream) {
		ExplicitStream<Simplex> result = new ExplicitStream<Simplex>(SimplexComparator.getInstance());
		List<Simplex> edges = new ArrayList<Simplex>();
		Set<Simplex> faces = new HashSet<Simplex>();
		Map<Simplex, Simplex> midPointMap = new HashMap<Simplex, Simplex>();
		
		int numPoints = 0;
		
		for (Simplex simplex: stream) {
			int dimension = stream.getDimension(simplex);
			if (dimension == 1) {
				edges.add(simplex);
			} else if (dimension == 0) {
				numPoints++;
				result.addElement(simplex, stream.getFiltrationIndex(simplex));
			} else if (dimension == 2) {
				faces.add(simplex);
			}
		}
		
		int pointIndex = numPoints;
		
		// subdivide each edge
		
		for (Simplex edge: edges) {
			int[] edgeVertices = edge.getVertices();
			int start = edgeVertices[0];
			int end = edgeVertices[1];
			Simplex startVertex = new Simplex(new int[]{start});
			Simplex endVertex = new Simplex(new int[]{end});
			Simplex midPoint = new Simplex(new int[]{pointIndex});
			int averageFiltrationIndex = (int) (0.5 * (stream.getFiltrationIndex(startVertex) + stream.getFiltrationIndex(endVertex)));
			result.addElement(midPoint, averageFiltrationIndex);
			midPointMap.put(edge, midPoint);
			Simplex edge1 = new Simplex(new int[]{start, pointIndex});
			Simplex edge2 = new Simplex(new int[]{end, pointIndex});
			result.addElement(edge1, averageFiltrationIndex);
			result.addElement(edge2, averageFiltrationIndex);
			pointIndex++;
		}
		
		for (Simplex face: faces) {
			int[] vertices = face.getVertices();
			Simplex edge1 = new Simplex(new int[]{vertices[0], vertices[1]});
			Simplex edge2 = new Simplex(new int[]{vertices[1], vertices[2]});
			Simplex edge3 = new Simplex(new int[]{vertices[0], vertices[2]});
			Simplex midpoint1 = midPointMap.get(edge1);
			Simplex midpoint2 = midPointMap.get(edge2);
			Simplex midpoint3 = midPointMap.get(edge3);
			
			Simplex face1 = new Simplex(new int[]{midpoint1.getVertices()[0], midpoint2.getVertices()[0], vertices[1]});
			Simplex face2 = new Simplex(new int[]{midpoint1.getVertices()[0], midpoint3.getVertices()[0], vertices[0]});
			Simplex face3 = new Simplex(new int[]{midpoint2.getVertices()[0], midpoint3.getVertices()[0], vertices[2]});
			Simplex face4 = new Simplex(new int[]{midpoint1.getVertices()[0], midpoint2.getVertices()[0], midpoint3.getVertices()[0]});
			
			result.addElement(face1, stream.getFiltrationIndex(face));
			result.addElement(face2, stream.getFiltrationIndex(face));
			result.addElement(face3, stream.getFiltrationIndex(face));
			result.addElement(face4, stream.getFiltrationIndex(face));
		}
		
		return result;
	}
}
