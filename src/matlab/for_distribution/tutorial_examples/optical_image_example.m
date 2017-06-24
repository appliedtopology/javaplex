% This shows the optical image patch example - Section 6

clc; clear; close all;
import edu.stanford.math.plex4.*;

% k = 300
load pointsOpticalDct_k300.mat
size(pointsOpticalDct_k300)

max_dimension = 3;
num_landmark_points = 50;
nu = 1;
num_divisions = 1000;

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(pointsOpticalDct_k300, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R / 4;

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
options.filename = 'lazyOpticalDct-k300';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

% plot a projection of the dataset onto the first two DCT basis vectors
figure
scatter(pointsOpticalDct_k300(:,1), pointsOpticalDct_k300(:,2), '.')
axis square

% k = 15
load pointsOpticalDct_k15.mat
size(pointsOpticalDct_k15)

max_dimension = 3;
num_landmark_points = 50;
nu = 1;
num_divisions = 500;

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(pointsOpticalDct_k15, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R / 4;

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
options.filename = 'lazyOpticalDct-k15';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

% plot a projection of the dataset onto the first two DCT basis vectors
figure
scatter(pointsOpticalDct_k15(:,1), pointsOpticalDct_k15(:,2), '.')
axis square