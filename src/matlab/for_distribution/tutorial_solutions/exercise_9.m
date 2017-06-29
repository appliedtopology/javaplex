% Exercise 9

%% This script demonstrates the Vietoris-Rips complex of 75 points randomly
%% sampled from a figure 8.

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 3;
max_filtration_value = 1.1;
num_divisions = 1000;

% Select 75 random points from the figure 8 space.
point_cloud = examples.PointCloudExamples.getRandomFigure8Points(75);
scatter(point_cloud(:,1),point_cloud(:,2)), axis equal

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'ripsFigure8';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);