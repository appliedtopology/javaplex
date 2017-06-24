% This script demonstrates how to extract endpoints

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 2;
max_filtration_value = 4;
num_divisions = 1000;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample();

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
intervals = persistence.computeIntervals(stream);

% The next line extracts the interval endpoints for the dimension 0
% barcode. Note that it ignores infinite intervals
endpoints_dim_0 = homology.barcodes.BarcodeUtility.getEndpoints(intervals, 0, false)

% The next line extracts the interval endpoints for the dimension 1
% barcode. Note that it ignores infinite intervals
endpoints_dim_1 = homology.barcodes.BarcodeUtility.getEndpoints(intervals, 1, false)

plot_barcodes(intervals);

