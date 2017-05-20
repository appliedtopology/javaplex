% This script calculates the intervals for a Lazy-Witness complex generated
% from random points on the 2-sphere

clc; clear; close all;
import edu.stanford.math.plex4.*;

dimension = 2;
num_points = 1000;
num_landmark_points = 100;
max_filtration_value = 0.01;

% create the set of points
%point_cloud = examples.PointCloudExamples.getRandomSpherePoints(num_points, dimension);
point_cloud = examples.PointCloudExamples.getRandomTorusPoints(num_points, 0.4, 0.8);

% create a randomized landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
stream = api.Plex4.createLazyWitnessStream(landmark_selector, dimension + 1, max_filtration_value);

% print out the size of the stream - will be quite large since the complex
% construction is very sensitive to the maximum filtration value
size = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute the intervals and transform them to filtration values
filtration_value_intervals = persistence.computeIntervals(stream);

%% Output

filename = 'sphere.pov';
create_pov_file(stream, point_cloud, filename);
render_pov_file(filename);
