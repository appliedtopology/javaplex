clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 2;
num_points = 30;
max_filtration_value = 0.9;
num_divisions = 1000;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSpherePoints(num_points, max_dimension);

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);

% compute the intervals and transform them to filtration values
filtration_value_intervals = persistence.computeIntervals(stream)


%%
dim_0_intervals = filtration_value_intervals.getIntervalsAtDimension(0)

minimum_length = 0.7;

long_dim_0_intervals = homology.barcodes.BarcodeUtility.filterIntervalsByMinimumLength(dim_0_intervals, minimum_length)