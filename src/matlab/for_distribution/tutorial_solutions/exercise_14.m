% Exercise 14

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 4;
max_filtration_value = 5;
num_divisions = 1000;

% Select the 8 vertices of the cube
point_cloud = [1,1,1;1,1,-1;1,-1,1;1,-1,-1;-1,1,1;-1,1,-1;-1,-1,1;-1,-1,-1];
scatter3(point_cloud(:,1),point_cloud(:,2),point_cloud(:,3)), view(100,10)

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
options.filename = 'ripsCube';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);