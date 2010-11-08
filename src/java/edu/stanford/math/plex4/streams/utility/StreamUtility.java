package edu.stanford.math.plex4.streams.utility;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;


public class StreamUtility {
	
	public static <T> List<T> getSkeleton(AbstractFilteredStream<T> stream, int dimension) {
		List<T> skeleton = new ArrayList<T>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == dimension) {
				skeleton.add(basisElement);
			}
		}
		
		return skeleton;
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
	
	public static <R, T> List<ObjectSparseFormalSum<R, T>> getBoundaryMatrixColumns(AbstractFilteredStream<T> stream, int dimension, ObjectAlgebraicFreeModule<R, T> chainModule) {
		List<ObjectSparseFormalSum<R, T>> boundaryMatrixColumns = new ArrayList<ObjectSparseFormalSum<R, T>>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == dimension) {
				boundaryMatrixColumns.add(chainModule.createNewSum(stream.getBoundaryCoefficients(basisElement), stream.getBoundary(basisElement)));
			}
		}
		
		return boundaryMatrixColumns;
	}
}
