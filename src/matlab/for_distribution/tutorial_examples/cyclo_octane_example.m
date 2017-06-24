% This shows the cyclo-octane example - Section 6

clc; clear; close all;
import edu.stanford.math.plex4.*;

load pointsCycloOctane.mat
size(pointsCycloOctane)

max_dimension = 3;
num_landmark_points = 100;
max_filtration_value = 0.5;
nu = 1;
num_divisions = 1000;

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(pointsCycloOctane, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()

% create a lazy witness stream
stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions);
stream.finalizeStream()

% print out the size of the stream
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'lazyCycloOctane';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);