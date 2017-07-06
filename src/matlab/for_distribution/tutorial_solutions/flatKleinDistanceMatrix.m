function distances = flatKleinDistanceMatrix(numPoints)

% INPUT:
%   numPoints - number of points
%
% OUTPUT:
%   distances - numPoints x numPoints distance matrix for randomly chosen 
%       points on the flat Klein bottle

import edu.stanford.math.plex4.*;

% sample numPoints random points from [0,1] x [0,1]
points = rand(numPoints, 2);

% create an empty matrix of size numPoints x numPoints
distances = zeros(numPoints);

for i = 1 : numPoints
    for j = i : numPoints;
        % compute the distance between points i and j in the induced metric
        % on the flat Klein bottle
        xDiff = abs(points(i, 1) - points(j, 1));
        yDiff1 = abs(points(i, 2) - points(j, 2));
        yDiff2 = abs(points(i, 2) - (1 - points(j, 2)));
        distances(i, j) = min( [norm([xDiff, yDiff1]), norm([xDiff, 1 - yDiff1]), norm([1 - xDiff, yDiff2]), norm([1 - xDiff, 1 - yDiff2])] );
        distances(j, i) = distances(i, j);
    end
end