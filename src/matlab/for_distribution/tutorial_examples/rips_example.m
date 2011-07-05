%% This script demonstrates the use of plex on a simple "house" example - 
%% Section 5.1

clc; clear; close all;

%% House Example

max_dimension = 3;
max_filtration_value = 4;
num_divisions = 100;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample();

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
filtration_index_intervals = persistence.computeIntervals(stream);
% this converts the filtration index intervals to the actual filtration
% value intervals
filtration_value_intervals = stream.transform(filtration_index_intervals);

% create the barcode plots
%api.Plex4.createBarcodePlot(filtration_value_intervals, 'ripsHouse', max_filtration_value)
plot_barcodes(filtration_value_intervals, 0, max_dimension - 1, 'ripsHouse');

%% Torus Example

max_dimension = 3;
max_filtration_value = 0.9;
num_divisions = 100;

% create the set of points
load pointsTorusGrid.mat
point_cloud = pointsTorusGrid;
size(point_cloud)

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);
num_simplices = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
filtration_index_intervals = persistence.computeIntervals(stream);
% this converts the filtration index intervals to the actual filtration
% value intervals
filtration_value_intervals = stream.transform(filtration_index_intervals);

% create the barcode plots
%api.Plex4.createBarcodePlot(filtration_value_intervals, 'ripsTorus', max_filtration_value)
plot_barcodes(filtration_value_intervals, 0, max_dimension - 1, 'ripsTorus', true);

% get the infinite barcodes
infinite_barcodes = filtration_value_intervals.getInfiniteIntervals();

% print out betti numbers array
betti_numbers_array = infinite_barcodes.getBettiSequence()