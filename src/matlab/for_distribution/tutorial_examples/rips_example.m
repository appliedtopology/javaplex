%% This script demonstrates the use of plex on a simple "house" example - 
%% Section 5.1

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% House Example

max_dimension = 3;
max_filtration_value = 4;
num_divisions = 1000;

% create the set of points
point_cloud = [-1,0; 1,0; 1,2; -1,2; 0,3];

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'ripsHouse';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

%% Torus Example

max_dimension = 3;
max_filtration_value = 0.9;
num_divisions = 1000;

% create the set of points
load pointsTorusGrid.mat
point_cloud = pointsTorusGrid;
size(point_cloud)

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'ripsTorus';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
options.side_by_side = true;
plot_barcodes(intervals, options);

% get the infinite barcodes
infinite_barcodes = intervals.getInfiniteIntervals();

% print out betti numbers array
betti_numbers_array = infinite_barcodes.getBettiSequence()