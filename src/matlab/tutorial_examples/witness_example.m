% This script calculates the intervals for a Witness complex - Section 5.3

clc; clear; close all;

dimension = 3;
num_points = 10000;
num_landmark_points = 50;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(num_points, 1, 2);
m_space = metric.impl.EuclideanMetricSpace(point_cloud)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2
max_filtration_value = r_max / 8

% create a randomized landmark selector
landmark_selector = api.Plex4.createRandomSelector(point_cloud, num_landmark_points);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
stream = api.Plex4.createWitnessStream(landmark_selector, dimension + 1, max_filtration_value);

% print out the size of the stream
size = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute the intervals and transform them to filtration values
filtration_index_intervals = persistence.computeIntervals(stream);
filtration_value_intervals = stream.transform(filtration_index_intervals)

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'witnessTorus', max_filtration_value)
