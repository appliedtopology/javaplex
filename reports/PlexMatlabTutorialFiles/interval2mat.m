function matrix = interval2mat(intervals)

% INPUT:
%   intervals - An array of instances of the JPlex Java class
%       PersistenceInterval.
%
% OUTPUT:
%   matrix - a matrix of size numIntervals x 3. Each row contains the
%       dimension, the start value, and the end value of a single interval.
%
% henrya@math.stanford.edu

import edu.stanford.math.plex.*;
numIntervals = size(intervals, 1);
matrix = zeros(numIntervals, 3);
for i = 1:numIntervals
    matrix(i,:) = [intervals(i).dimension, intervals(i).start, intervals(i).end];
end