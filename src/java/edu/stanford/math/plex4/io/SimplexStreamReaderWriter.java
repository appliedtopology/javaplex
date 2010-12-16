package edu.stanford.math.plex4.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

public class SimplexStreamReaderWriter implements ObjectReader<AbstractFilteredStream<Simplex>>, ObjectWriter<AbstractFilteredStream<Simplex>> {
	private static final SimplexStreamReaderWriter instance = new SimplexStreamReaderWriter();

	private SimplexStreamReaderWriter() {}

	public static SimplexStreamReaderWriter getInstance() {
		return instance;
	}

	public String getExtension() {
		return "txt";
	}
	
	public AbstractFilteredStream<Simplex> importFromFile(String path) throws IOException {
		ExplicitSimplexStream stream = new ExplicitSimplexStream();
		BufferedReader reader = null;
		String line = null;
		String[] entries = null;
		String separator = ",";
		try {
			reader = new BufferedReader(new FileReader(path));
			
			// format:
			// <filtrationIndex>,<vertex0>, ... <vertexN>
			
			// continue reading the data
			while ((line = reader.readLine()) != null) {
				// split the line into the individual tokens
				entries = line.split(separator);
				int filtrationIndex = Integer.parseInt(entries[0]);
				int[] vertices = new int[entries.length - 1];
				for (int i = 0; i < vertices.length; i++) {
					vertices[i] = Integer.parseInt(entries[i + 1]);
				}
				stream.addElement(vertices, filtrationIndex);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the reader if necessary
			if (reader != null) {
				reader.close();
			}
		}
		
		return stream;
	}

	public void writeToFile(AbstractFilteredStream<Simplex> object, String path) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path, false));
			for (Simplex simplex: object) {
				Integer filtrationIndex = object.getFiltrationIndex(simplex);
				writer.write(filtrationIndex.toString() + ",");
				int[] vertices = simplex.getVertices();
				for (int i = 0; i < vertices.length; i++) {
					Integer vertex = vertices[i];
					writer.write(vertex.toString());
					if (i < vertices.length - 1) {
						writer.write(",");
					}
				}
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
