clc; clear; close all;
import edu.stanford.math.plex4.*;

dimension = 1;
max_filtration_value = 1.3;
num_divisions = 20;

n = 20;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSpherePoints(n, dimension);

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute intervals
intervals = persistence.computeIntervals(stream);

% create the barcode plots
plot_barcodes(intervals)