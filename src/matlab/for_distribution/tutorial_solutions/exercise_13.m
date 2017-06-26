% Exercise 13

% Our planar dataset is 14 evenly spaced points on the unit circle. We 
% build a Vietoris-Rips stream which, at the correct filtration value, is 
% homeomorphic to the 6-sphere. It has 14 vertices because it is obtained 
% by suspending the 0-sphere six times, for a total of 2 + (6 * 2) = 14 
% vertices.

clc; clear; close all;
import edu.stanford.math.plex4.*;

n = 14;

angles = 2 * pi/n * (1:n)';
pointsS1 = [cos(angles), sin(angles)];
scatter(pointsS1(:,1), pointsS1(:,2)), axis equal

max_dimension = 7;
max_filtration_value = 2;
num_divisions = 1000;

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(pointsS1, max_dimension + 1, max_filtration_value, num_divisions);

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension + 1, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)
