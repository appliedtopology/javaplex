package edu.stanford.math.plex4.homology;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.derived.HomStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;


public class HomTester {
	
	public static void dumpHomInformation(AbstractFilteredStream<Simplex> domainStream, AbstractFilteredStream<Simplex> codomainStream) {
		HomStream<Simplex, Simplex> homStream = new HomStream<Simplex, Simplex>(domainStream, codomainStream);
		List<IntSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>> homotopies = homStream.getHomotopies();
		
		System.out.println("Homotopies:");
		for (IntSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			System.out.println("$$" + homotopy + "$$");
		}
		
		ObjectPersistenceAlgorithm<Fraction, ObjectObjectPair<Simplex, Simplex>> persistence = Plex4.getRationalHomAlgorithm();
		ObjectAlgebraicFreeModule<Fraction, ObjectObjectPair<Simplex, Simplex>> chain_module = persistence.getChainModule();
		
		IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>> barcode_collection = persistence.computeAnnotatedIntervals(homStream);

	    List<ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>>> intervals = barcode_collection.getBarcode(0).getIntervals();

	    ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>> cycle_sum = chain_module.createNewSum();

	    Iterator<ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>>> iterator = intervals.iterator();
	    ObjectObjectPair<IntHalfOpenInterval, ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>>> interval_generator_pair;

	    System.out.println("Generating Cycles:");
	    
	    while (iterator.hasNext()) {
	        interval_generator_pair = iterator.next();
	        IntHalfOpenInterval interval = interval_generator_pair.getFirst();
	        ObjectSparseFormalSum<Fraction, ObjectObjectPair<Simplex, Simplex>> generator = interval_generator_pair.getSecond();
	        if (interval.isInfinite()) {
	            chain_module.accumulate(cycle_sum, generator);
	            System.out.println("$$" + generator + "$$");
	        }
	    }
	    
	    System.out.println("Cycle Sum:");
	    System.out.println("$$" + cycle_sum + "$$");
	    
	    ObjectAbsoluteHomology<Fraction, Simplex> basic_persistence = Plex4.getRationalSimplicialAlgorithm(2);
	    
	    System.out.println(basic_persistence.computeAnnotatedIntervals(domainStream));
	    System.out.println(basic_persistence.computeAnnotatedIntervals(codomainStream));
	}
}
