% Exercise 5.1.2

% Our planar dataset is 6 evenly spaced points on the unit circle. We build
% a Vietoris-Rips stream which, at the correct filtration value, is an 
% octahedron.

clc; clear; close all;

n = 6;

angles = 2 * pi/n * (1:n)';
pointsS1 = [cos(angles), sin(angles)];
scatter(pointsS1(:,1), pointsS1(:,2), '.')

max_dimension = 2;
max_filtration_value = 2;
num_divisions = 100;

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(pointsS1, max_dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension + 1);

% this initially creates a set of intervals which contains the filtration
% indices (which are integers).
filtration_value_intervals = persistence.computeIntervals(stream)
