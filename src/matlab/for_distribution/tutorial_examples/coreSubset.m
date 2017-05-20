function core = coreSubset(points, densities, numPoints)

% INPUT:
%   points - N x n matrix of N points in R^n
%   densities - vertical vector of length N whose i-th entry is the 
%       estimated density of the i-th point.
%   numPoints - nonnegative integer less than or equal to N
%
% OUTPUT:
%   core - numPoints x n matrix of the top numPoints densest points (as 
%       ranked by densities)

import edu.stanford.math.plex4.*;

[A,sortedDensityIndices] = sort(densities,'descend');
core = points(sortedDensityIndices(1:numPoints),:);