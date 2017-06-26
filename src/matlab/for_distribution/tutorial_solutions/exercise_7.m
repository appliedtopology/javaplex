% Exercise 7

% select points from the unit sphere S^2 and then compute the distance
% matrix for these points under the induced metric on the projective plane

clc; clear; close all;
import edu.stanford.math.plex4.*;

num_points = 1000;
distances = projPlaneDistanceMatrix(num_points);

% create an explicit metric space from this distance matrix
m_space = metric.impl.ExplicitMetricSpace(distances);