%% This script demonstrates the use of plex on a simple "house" example - Section 5.1

% Important Note: Instead of adjusting the delta value as in JPlex, one
% adjusts the number of divisions in JavaPlex. In other words, we have the
% relation N x delta = t_max. The reason why this choice was made was
% because if t_max changes (e.g. if the scale of the problem changes) then
% one must also adjust delta. However, one can set N to be some reasonable
% number (for example 10 or 100), and forget about it since it does not
% have to be rescaled.

clc; clear; close all;

max_dimension = 2;
max_filtration_value = 4;
num_divisions = 100;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample();

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
filtration_index_intervals = persistence.computeIntervals(stream)
% this converts the filtration index intervals to the actual filtration
% value intervals
filtration_value_intervals = stream.transform(filtration_index_intervals)

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'ripsHouse', 4)


%% Torus Example

max_dimension = 2;
max_filtration_value = 0.9;
num_divisions = 20;
num_points = 400;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(num_points, 1, 2);

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
filtration_index_intervals = persistence.computeIntervals(stream);
% this converts the filtration index intervals to the actual filtration
% value intervals
filtration_value_intervals = stream.transform(filtration_index_intervals);

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'ripsTorus', 4)

% get the infinite barcodes
infinite_barcodes = filtration_value_intervals.getInfiniteIntervals();

% print out betti numbers array
betti_numbers_array = infinite_barcodes.getBettiSequence()

% print out betti numbers in form {dimension: betti number}
betti_numbers_string = infinite_barcodes.getBettiNumbers()