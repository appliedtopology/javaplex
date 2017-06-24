% This shows the range image patch example - Section 6

clc; clear; close all;
import edu.stanford.math.plex4.*;

load pointsRange.mat
size(pointsRange)

max_dimension = 3;
num_landmark_points = 50;
nu = 1;
num_divisions = 1000;

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

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'lazyRange';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

%% DCT

pointsRangeDct = pointsRange * dct(5);

figure;
scatter(pointsRangeDct(:,1), pointsRangeDct(:,5), '.')
axis square
