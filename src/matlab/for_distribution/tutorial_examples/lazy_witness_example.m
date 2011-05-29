% This script calculates the intervals for a Lazy-Witness complex generated
% from random points on the 2-sphere - Section 5.4

clc; clear; close all;

max_dimension = 3;
num_points = 1000;
num_landmark_points = 100;
max_filtration_value = 0.4;
nu = 1;
% error - int nu is interpreted as num_divisions!! Having trouble
% using both nu and num_divisions as inputs. 

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSpherePoints(num_points, max_dimension - 1);

% create a randomized landmark selector
landmark_selector = api.Plex4.createRandomSelector(point_cloud, num_landmark_points);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
stream = api.Plex4.createLazyWitnessStream(landmark_selector, max_dimension, max_filtration_value, nu);

% print out the size of the stream - will be quite large since the complex
% construction is very sensitive to the maximum filtration value
size = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% compute the intervals and transform them to filtration values
filtration_index_intervals = persistence.computeIntervals(stream);
filtration_value_intervals = stream.transform(filtration_index_intervals);

% create the barcode plots
api.Plex4.createBarcodePlot(filtration_value_intervals, 'lazySphere', max_filtration_value)
