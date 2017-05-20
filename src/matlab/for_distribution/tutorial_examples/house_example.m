% This script shows the house example - Section 3.2

%% House Example

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% construct the complex
stream.addVertex(1, 0);
stream.addVertex(2, 0);
stream.addVertex(3, 0);
stream.addVertex(4, 0);
stream.addVertex(5, 1);
stream.addElement([1, 2], 0);
stream.addElement([2, 3], 0);
stream.addElement([3, 4], 0);
stream.addElement([4, 1], 0);
stream.addElement([3, 5], 2);
stream.addElement([4, 5], 3);
stream.addElement([3, 4, 5], 7);
stream.finalizeStream();

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)

% store the intervals as Matlab matrices
intervals_dim0 = edu.stanford.math.plex4.homology.barcodes.BarcodeUtility.getEndpoints(intervals, 0, 0)
intervals_dim1 = edu.stanford.math.plex4.homology.barcodes.BarcodeUtility.getEndpoints(intervals, 1, 0)

% compute and print the intervals annotated with a representative cycle
intervals = persistence.computeAnnotatedIntervals(stream)

% get the infinite barcodes
infinite_barcodes = intervals.getInfiniteIntervals()

% print out betti numbers array
betti_numbers_array = infinite_barcodes.getBettiSequence()

% print out betti numbers in form {dimension: betti number}
betti_numbers_string = infinite_barcodes.getBettiNumbers()

% create the barcode plots
options.filename = 'house';
options.max_filtration_value = 8;
options.file_format = 'eps';
plot_barcodes(intervals, options);

% validate
stream.validateVerbose()

% add illegal simplex
stream.addElement([1, 4, 5], 0);

% validate again
stream.validateVerbose()
