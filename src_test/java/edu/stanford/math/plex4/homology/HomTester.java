package edu.stanford.math.plex4.homology;


public class HomTester {
	/*
	public static <R, T, U> void verifyHomotopies(AbstractFilteredStream<T> domainStream, AbstractFilteredStream<U> codomainStream,
			ObjectAlgebraicFreeModule<R, T> chainModule) {
		
		HomStream<T, U> homStream = new HomStream<T, U>(domainStream, codomainStream);
		List<ObjectSparseFormalSum<R, ObjectObjectPair<T, U>>> homotopies = homStream.getHomotopies(chainModule);

		ObjectMatrixConverter<R, T, U> matrixConverter = new ObjectMatrixConverter<R, T, U>(domainStream, codomainStream);
		
		// We must verify that d(f(s)) = f(d(s)) where d is the boundary operator, and f is the homotopy, and s ranges
		// over the domain complex. 
		for (ObjectSparseFormalSum<R, ObjectObjectPair<T, U>> homotopy: homotopies) {
			ObjectSparseMatrix<R> homotopyMatrix = matrixConverter.toSparseMatrix(homotopy);
			
			for (T domainElement: domainStream) {
				ObjectSparseVector<R> domainElementVector = matrixConverter.getDomainRepresentation().toSparseVector(chainModule.createNewSum(domainElement));
				
				ObjectSparseFormalSum<R, T> domainBoundaryChain = chainModule.createNewSum(domainStream.getBoundaryCoefficients(domainElement), domainStream.getBoundary(domainElement));
				ObjectSparseVector<R> domainBoundaryVector = matrixConverter.getDomainRepresentation().toSparseVector(domainBoundaryChain);
				
				//ObjectSparseVector<R> RHS = 
			}
		}
		
		
	}
	*/
}
