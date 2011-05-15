% This shows the image patch example - Section 6

clc; clear; close all;

load pointsRange.mat
size(pointsRange)


dimension = 2;
num_landmark_points = 50;
num_divisions = 500;

m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2
max_filtration_value = r_max / 5

% create a max-min landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(pointsRange, num_landmark_points);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
stream = api.Plex4.createLazyWitnessStream(landmark_selector, dimension + 1, max_filtration_value, num_divisions);

% print out the size of the stream - will be quite large since the complex
% construction is very sensitive to the maximum filtration value
size = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute the intervals and transform them to filtration values
filtration_index_intervals = persistence.computeIntervals(stream);
filtration_value_intervals = stream.transform(filtration_index_intervals)

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'lazRange', max_filtration_value)


%% DCT

pointsRangeDct = pointsRange * dct(5);

plot(pointsRangeDct(:,1), pointsRangeDct(:,5), '.')
axis square
