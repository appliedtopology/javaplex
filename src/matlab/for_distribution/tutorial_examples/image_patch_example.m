% This shows the image patch example - Section 6

clc; clear; close all;

load pointsRange.mat
size(pointsRange)

max_dimension = 3;
num_landmark_points = 50;
nu = 1;
num_divisions = 500;

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(pointsRange, num_landmark_points);
R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R / 3;

% create a lazy witness stream
stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions);
stream.finalizeStream()

% print out the size of the stream - will be quite large since the complex
% construction is very sensitive to the maximum filtration value
num_simplices = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% compute the intervals and transform them to filtration values
filtration_value_intervals = persistence.computeIntervals(stream);

% create the barcode plots
%api.Plex4.createBarcodePlot(filtration_value_intervals, 'lazyRange', max_filtration_value)
plot_barcodes(filtration_value_intervals, struct('filename', 'lazyRange'));

%% DCT

pointsRangeDct = pointsRange * dct(5);

figure;
scatter(pointsRangeDct(:,1), pointsRangeDct(:,5), '.')
axis square
