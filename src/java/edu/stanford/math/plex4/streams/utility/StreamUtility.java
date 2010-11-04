package edu.stanford.math.plex4.streams.utility;


public class StreamUtility {
	/*
	public static <T> List<T> getModuleBasis(AbstractFilteredStream<T> stream, int dimension, boolean increasingDimensionality) {
		List<T> basis = new ArrayList<T>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == dimension) {
				basis.add(basisElement);
			} else if (elementDimension > dimension && increasingDimensionality) {
				break;
			}
		}
		return basis;
	}
	
	public static <T> List<T> getAllBasisElements(AbstractFilteredStream<T> stream) {
		List<T> basis = new ArrayList<T>();
		
		for (T basisElement: stream) {
			basis.add(basisElement);
		}
		
		return basis;
	}
	
	public static <T> int getSkeletonSize(AbstractFilteredStream<T> stream, int dimension) {
		int size = 0;
		
		for (T basisElement: stream) {
			if (stream.getDimension(basisElement) == dimension) {
				size++;
			}
		}
		
		return size;
	}
	
	public static <T> int getSize(AbstractFilteredStream<T> stream) {
		if (stream instanceof PrimitiveStream<?>) {
			PrimitiveStream<?> primitiveStream = (PrimitiveStream<?>) stream;
			return primitiveStream.getSize();
		}
		
		int size = 0;
		
		for (T basisElement: stream) {
			size++;
		}
		
		return size;
	}
	*/
}
