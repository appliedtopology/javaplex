function indices = coreSubset(densities, numPoints)

% INPUT:
%   points - N x n matrix of N points in R^n
%   densities - vertical vector of length N whose i-th entry is the 
%       estimated density of the i-th point.
%   numPoints - nonnegative integer less than or equal to N
%
% OUTPUT:
%   indices - numPoints x 1 matrix of the indices of the top numPoints densest 
%   points (as ranked by densities)
%
% henrya@math.stanford.edu, with mods by atausz@stanford.edu

[A,sortedDensityIndices] = sort(densities,'descend');
indices = sortedDensityIndices(1:numPoints);