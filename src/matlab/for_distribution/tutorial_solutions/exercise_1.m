% Exercise 1

% We order the vertices in a clockwise fashion, starting with
% vertex 1 on the top left.

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% add simplices
for i = 1:6
    stream.addVertex(i);
end

stream.addElement([1, 2]);
stream.addElement([1, 6]);
stream.addElement([2, 3]);
stream.addElement([2, 5]);
stream.addElement([3, 4]);
stream.addElement([3, 5]);
stream.addElement([4, 5]);
stream.addElement([5, 6]);

stream.addElement([2, 3, 5]);

stream.finalizeStream();

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)
