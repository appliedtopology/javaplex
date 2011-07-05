% Exercise 5.4.5

clc; clear; close all;

max_dimension = 3;
num_points = 1000;
num_landmark_points = 50;
nu = 1;
num_divisions = 100;

% Select points from the square [0,1] x [0,1] and then compute the distance
% matrix for these points under the induced metric on the flat Klein bottle
distances = flatKleinDistanceMatrix(num_points);

% Create an explicit metric space from this distance matrix
m_space = metric.impl.ExplicitMetricSpace(distances);

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4. createMaxMinSelector(m_space, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R;

% create a lazy witness stream
stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions);
stream.finalizeStream()

% print out the size of the stream
num_simplices = stream.getSize()

% get homology algorithm over Z/2Z
Z2_persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals and transform them to filtration values
Z2_filtration_value_intervals = Z2_persistence.computeIntervals(stream);

% create the barcode plots
%api.Plex4.createBarcodePlot(Z2_filtration_value_intervals, 'lazyWitnessFlatKlein_Z2', max_filtration_value)
plot_barcodes(Z2_filtration_value_intervals, 0, 2, 'lazyWitnessFlatKlein Z2');

% get homology algorithm over Z/2Z
Z3_persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 3);

% compute the intervals and transform them to filtration values
Z3_filtration_value_intervals = Z3_persistence.computeIntervals(stream);
% create the barcode plots
%api.Plex4.createBarcodePlot(Z3_filtration_value_intervals, 'lazyWitnessFlatKlein_Z3', max_filtration_value);

plot_barcodes(Z3_filtration_value_intervals, 0, 2, 'lazyWitnessFlatKlein Z3');
