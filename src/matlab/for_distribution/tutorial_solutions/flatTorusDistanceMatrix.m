function distances = flatTorusDistanceMatrix(numPoints)

% INPUT:
%   numPoints - number of points
%
% OUTPUT:
%   distances - numPoints x numPoints distance matrix for randomly chosen 
%       points on the flat torus

import edu.stanford.math.plex4.*;

% sample numPoints random points from [0,1] x [0,1]
points = rand(numPoints, 2);

% create an empty matrix of size numPoints x numPoints
distances = zeros(numPoints);

for i = 1 : numPoints
    for j = i : numPoints;
        % compute the distance between points i and j in the induced metric
        % on the flat torus
        xDiff = abs(points(i, 1) - points(j, 1));
        yDiff = abs(points(i, 2) - points(j, 2));
        distances(i, j) = sqrt((min(xDiff, 1 - xDiff))^2 + (min(yDiff, 1 - yDiff))^2);
        distances(j, i) = distances(i, j);
    end
end