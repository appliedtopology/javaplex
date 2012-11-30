/**
 * 
 */
package edu.stanford.math.plex4.graph.random;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * Makes a clustered graph by connecting a specified number of clusters (i.e. 
 * ErdosRenyi graphs) in a specified way.
 * 
 * @author Tim Harrington
 * @date Oct 15, 2009
 */
public class ClusteredERGraph extends GraphInstanceGenerator {

	private static final long serialVersionUID = -1685211333671788823L;
	// variables for defining the clusters
	int[] clusterSizes; // how many nodes in each cluster
	double[] clusterParams; // the ER-parameter for each cluster
	
	// variables for specifiying how the clusters should be connected
	List<Integer> srcClusters = new ArrayList<Integer>();
	List<Integer> destClusters = new ArrayList<Integer>();
	List<Integer> edgeCounts = new ArrayList<Integer>(); // as a % of nodes in source cluster

	// node count
	int n = 0;
	
	public ClusteredERGraph(int clusterCount) {
		this.clusterSizes = new int[clusterCount];
		this.clusterParams = new double[clusterCount];
	}
	
	/**
	 * Warning: No input validation is performed. 
	 * 
	 * @param index
	 * @param size
	 * @param param
	 */
	public void defineCluster(int index, int size, double param) {
		n += size;
		clusterSizes[index] = size;
		clusterParams[index] = param;
	}

	/**
	 * Warning: No input validation is performed. 
	 * 
	 * @param srcIndex
	 * @param destIndex
	 * @param param
	 */
	public void linkClusters(int srcIndex, int destIndex, int edgeCount) {
		srcClusters.add(Integer.valueOf(srcIndex));
		destClusters.add(Integer.valueOf(destIndex));
		edgeCounts.add(Integer.valueOf(edgeCount));
	}
	
	@Override
	public AbstractUndirectedGraph generate() {

		AbstractUndirectedGraph graph = this.initializeGraph(n);
		
		// store the cluster starting points
		int[] startingPoints = new int[clusterSizes.length];
		
		// make the clusters
		int start = 0; // stores the next cluster starting point
		for (int i=0; i<clusterSizes.length; i++) {
			makeClusterInPlace(graph,start,start+clusterSizes[i],clusterParams[i]);
			startingPoints[i] = start;
			start += clusterSizes[i];
		}
		
		// link the clusters
		int srcId;
		int dstId;
		for (int j=0; j<srcClusters.size(); j++) {
			srcId = srcClusters.get(j); 
			dstId = destClusters.get(j);
			linkClustersInPlace(graph,
					startingPoints[srcId],startingPoints[srcId]+ clusterSizes[srcId],
					startingPoints[dstId],startingPoints[dstId]+ clusterSizes[dstId],
					edgeCounts.get(j));
		}
		
		return graph;
	}

	/**
	 * @param graph graph to add edges to
	 * @param srcStart Start of the source cluster range (inclusive)
	 * @param srcEnd End of the source cluster range (exlusive)
	 * @param dstStart Start of the dest cluster range (inclusive)
	 * @param dstEnd End of the dest cluster range (exlusive)
	 * @param edgeCount number of edges to randomly make
	 */
	protected void linkClustersInPlace(AbstractUndirectedGraph graph, int srcStart, int srcEnd, 
			int dstStart, int dstEnd, int edgeCount) {
		int src;
		int dst;
		for (int i=0; i<edgeCount; i++) {
			src = getRandomIntInRange(srcStart,srcEnd);
			dst = getRandomIntInRange(dstStart,dstEnd);
			graph.addEdge(src,dst);
		}
    }

	/**
	 * @param x0 start of range (inclusive)
	 * @param x1 end of range (exclusive)
	 * @return a random integer in [x0,x1) 
	 */
	protected int getRandomIntInRange(int x0, int x1) {
		return RandomUtility.nextUniformInt(x0,x1-1);
	}
	
	/**
	 * @param graph the graph to add edges to
	 * @param startNode the first node in the cluster (inclusive)
	 * @param endNode one greater than the last node in the cluster
	 * @param p edge probability
	 */
	protected void makeClusterInPlace(AbstractUndirectedGraph graph, int startNode, 
			int endNode, double p) {
		for (int i = startNode; i < endNode; i++) {
			for (int j = startNode; j < endNode; j++) {
				// don't make loops
				if (i == j)
					continue;
				// add edge with probability p
				if (RandomUtility.nextBernoulli(p) == 1) {
					graph.addEdge(i, j);
				}
			}
		}
	}
	
	public String explain() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb, Locale.US);
		sb.append("[");
		for (int i=0; i<clusterSizes.length; i++) {
			f.format("{%s,%s}",clusterSizes[i],clusterParams[i]);
			if (i==clusterSizes.length-1) {
				sb.append("|");
			} else {
				sb.append(",");
			}
		}
		for (int i=0; i<srcClusters.size(); i++) {
			f.format("{%s,%s,%s}",srcClusters.get(i),
					destClusters.get(i),edgeCounts.get(i));
			if (i==srcClusters.size()-1) {
				sb.append("]");
			} else {
				sb.append(",");
			}
		}
		f.close();
		return sb.toString();
	}

	@Override
    public String toString() {
	    return "ClusteredERGraph(" + explain() + ")";
    }
	
	
	
}
