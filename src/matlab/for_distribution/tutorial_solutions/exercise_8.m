% Exercise 8

%% This script demonstrates the Vietoris-Rips complex on the "house with a
%% garage"

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 4;
max_filtration_value = 5;
num_divisions = 1000;

% Select n evenly-spaced points from the circle
n=9;
point_cloud = [-1,0;1,0;1,2;-1,2;0,3;4,0;4,2];
scatter(point_cloud(:,1),point_cloud(:,2)), axis equal

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'ripsHouseWithGarage';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);