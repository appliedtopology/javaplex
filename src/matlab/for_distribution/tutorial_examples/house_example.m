% This script shows the house example - Section 3.3

%% Manual construction

clc; clear; close all;

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

% print out the total number of simplices in the complex
size = stream.getSize()

% get the default persistence computation
persistence = api.Plex4.getDefaultSimplicialAlgorithm(3);

% compute intervals
filtration_index_intervals = persistence.computeIntervals(stream);

% transform integral intervals to floating point intervals
transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(filtration_index_intervals)

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'house', 8)

% get the infinite barcodes
infinite_barcodes = filtration_value_intervals.getInfiniteIntervals();

% print out betti numbers array
betti_numbers_array = infinite_barcodes.getBettiSequence()

% print out betti numbers in form {dimension: betti number}
betti_numbers_string = infinite_barcodes.getBettiNumbers()

%% Validation - Section 3.4

% validate
stream.validate();

% add illegal simplex
stream.addElement([1, 4, 5], 0);

% validate again
stream.validate();
stream.validateVerbose();
