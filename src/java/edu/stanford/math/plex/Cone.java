package edu.stanford.math.plex;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The class Cone provides support for computing persistence across a 
 * filtration as well as a cone over this filtration. This has uses in 
 * computing relative homology as well as in computing cohomology and 
 * relative cohomology. See de Silva-Vejdemo Johansson (forthcoming) for 
 * details.
 */
public class Cone extends SimplexStream {
	protected SimplexStream sstream;
	protected boolean coning;
	protected int conepoint;
	protected Iterator<Simplex> ssiterator;
	protected HashMap<Integer,Boolean> support;
	protected int findexOffset;

	/**
	 * Creates a new cone from an existing SimplexStream. 
	 *
	 * @param sstr Simplex Stream to be coned.
	 */
	public Cone(SimplexStream sstr) {
		sstream = sstr;
		coning = false;
		conepoint = 0;
		ssiterator = sstream.iterator();
		support = new HashMap<Integer,Boolean>();
	}

	// Interface functions defined by SimplexStream
	public boolean hasNext() {
		if(coning)
			return ssiterator.hasNext();
		else
			return (sstream.size() > 0);
	}

	public Simplex next() {
		Simplex s;
		if(ssiterator.hasNext()) {
			s = ssiterator.next();
		} else {
			if(coning) 
				return null;
			else {
				// We reached the end of the ordinary iteration. Now we need
				// to start with coning simplices.
				coning = true;
				ssiterator = sstream.iterator();
				Set<Integer> supportpoints = support.keySet();
				Integer maxpoint = Collections.max(supportpoints);
				conepoint = maxpoint+1;
				s = Simplex.getSimplex(new int[0],0);
			}
		} 
		if(coning) {
			Simplex t = s.copy();
			t = t.addVertex(conepoint);
			t.setfindex(s.findex()+findexOffset);
			s = t;
		} else {
			int[] vertices = s.vertices();
			for(int i = 0; i<vertices.length; i++) 
				support.put(vertices[i],true);
			findexOffset = s.findex()+1;
		}
		return s;
	}

	public int size() {
		return 2*sstream.size()+1;
	}
	public int maxDimension() {
		return sstream.maxDimension()+1;
	}

	public Iterator<Simplex> iterator() {
		return this;
	}
}
