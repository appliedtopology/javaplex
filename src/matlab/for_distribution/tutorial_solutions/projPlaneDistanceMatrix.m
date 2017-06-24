function distances = projPlaneDistanceMatrix(numPoints)

% INPUT:
%   numPoints - number of points
%
% OUTPUT:
%   distances - numPoints x numPoints distance matrix for randomly chosen 
%       points on the projective plane

import edu.stanford.math.plex4.*;

% sample points from the unit sphere S^2
point_cloud = zeros(numPoints, 3);
for i = 1 : numPoints
    point = randn(1, 3);
    while norm(point) == 0
        point = randn(1, 3);
    end
    point_cloud(i, :) = point / norm(point);
end

% create an empty matrix of size numPoints x numPoints
distances = zeros(numPoints);

for i = 1 : numPoints
    for j = i : numPoints
        % compute the distance between points i and j in the induced metric
        % on the projective plane
        dist1 = norm(point_cloud(i, :) - point_cloud(j, :));
        dist2 = norm(point_cloud(i, :) + point_cloud(j, :));
        distances(i, j) = min(dist1, dist2);
        distances(j, i) = distances(i, j);
    end
end