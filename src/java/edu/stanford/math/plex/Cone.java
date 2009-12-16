//
//  Cone.java
//
//  ***************************************************************************
//
//  Copyright 2008, Stanford University
//
//  Permission to use, copy, modify, and distribute this software and its
//  documentation for any purpose and without fee is hereby granted,
//  provided that the above copyright notice appear in all copies and that
//  both that copyright notice and this permission notice appear in
//  supporting documentation, and that the name of Stanford University not
//  be used in advertising or publicity pertaining to distribution of the
//  software without specific, written prior permission.  Stanford
//  University makes no representations about the suitability of this
//  software for any purpose.  It is provided "as is" without express or
//  implied warranty.
//
//  ***************************************************************************
//

package edu.stanford.math.plex;
import java.util.*;

/**
 * The class Cone provides support for computing persistence across a 
 * filtration as well as a cone over this filtration. This has uses in 
 * computing relative homology as well as in computing cohomology and 
 * relative cohomology. See de Silva-Vejdemo Johansson (forthcoming) for 
 * details.
 */

public class Cone extends SimplexStream {
    private SimplexStream sstream;
    private boolean coning;
    private int conepoint;
    private Iterator<Simplex> ssiterator;
    private HashMap<Integer,Boolean> support;
    private int findexOffset;

    /**
     * Creates a new cone from an existing SimplexStream. 
     *
     * @param sstr Simplex Stream to be coned.
     * @return A new Cone object
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
