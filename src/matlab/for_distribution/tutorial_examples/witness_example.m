% This script calculates the intervals for a witness complex - Section 5.3

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% Torus Example

num_points = 10000;
num_landmark_points = 50;
max_dimension = 3;
num_divisions = 1000;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(num_points, 1, 2);

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R / 8;

% create a witness stream
stream = api.Plex4.createWitnessStream(landmark_selector, max_dimension, max_filtration_value, num_divisions);

% print out the size of the stream
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'witnessTorus';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);