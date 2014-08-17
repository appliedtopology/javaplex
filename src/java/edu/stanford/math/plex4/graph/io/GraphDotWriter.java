package edu.stanford.math.plex4.graph.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.io.ObjectWriter;
import edu.stanford.math.primitivelib.autogen.pair.IntIntPair;

public class GraphDotWriter implements ObjectWriter<AbstractUndirectedGraph> {

	public void writeToFile(AbstractUndirectedGraph object, String path) throws IOException {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(path, false));
			
			writer.write("graph {");
			writer.newLine();
			
			for (IntIntPair edge : object) {
				writer.write(edge.getFirst() +  " -- " + edge.getSecond() + ";");
				writer.newLine();
			}
		
			writer.write("}");
			writer.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public String getExtension() {
		return ".dot";
	}

}
