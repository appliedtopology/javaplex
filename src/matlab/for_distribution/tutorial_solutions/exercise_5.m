% Exercise 5

% select points from the square [0,1] x [0,1] and then compute the distance
% matrix for these points under the induced metric on the flat Klein bottle
num_points = 1000;
distances = flatKleinDistanceMatrix(num_points);

% create an explicit metric space from this distance matrix
m_space = metric.impl.ExplicitMetricSpace(distances);