% This script demonstrates the use of plex on a simple "house" example

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
plot_barcodes(filtration_value_intervals, 0, max_dimension, 'house_example')