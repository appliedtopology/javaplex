% This script calculates the intervals for a Lazy-Witness complex generated
% from random points on the 2-sphere

clc; clear; close all;
import edu.stanford.math.plex4.*;

dimension = 2;
num_points = 1000;
num_landmark_points = 80;
max_filtration_value = 0.1;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomSpherePoints(num_points, dimension);

% create a maxmin-landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
% create a Lazy-Witness Stream
stream = api.Plex4.createLazyWitnessStream(landmark_selector, dimension + 1, max_filtration_value);

% print out the size of the stream - will be quite large since the complex
% construction is very sensitive to the maximum filtration value
size = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute the intervals and transform them to filtration values
filtration_value_intervals = persistence.computeIntervals(stream);

%% Output

render_onscreen(stream, point_cloud);