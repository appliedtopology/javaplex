% Exercise 3

% We use the minimal triangulation for the projective plane, which contains
% 6 vertices. For a picture, see "javaplex_tutorial_solutions.pdf".

clc; clear; close all;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% add simplices
for i = 1:6
    stream.addVertex(i);
end

stream.addElement([1, 2]);
stream.addElement([1, 3]);
stream.addElement([1, 4]);
stream.addElement([1, 5]);
stream.addElement([1, 6]);
stream.addElement([2, 3]);
stream.addElement([2, 4]);
stream.addElement([2, 5]);
stream.addElement([2, 6]);
stream.addElement([3, 4]);
stream.addElement([3, 5]);
stream.addElement([3, 6]);
stream.addElement([4, 5]);
stream.addElement([4, 6]);
stream.addElement([5, 6]);

stream.addElement([1, 2, 5]);
stream.addElement([1, 2, 6]);
stream.addElement([1, 3, 4]);
stream.addElement([1, 3, 6]);
stream.addElement([1, 4, 5]);
stream.addElement([2, 3, 4]);
stream.addElement([2, 3, 5]);
stream.addElement([2, 4, 6]);
stream.addElement([3, 5, 6]);
stream.addElement([4, 5, 6]);

stream.finalizeStream();

% get persistence algorithm over Z/2Z
Z2_persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
Z2_intervals = Z2_persistence.computeIntervals(stream)

% get persistence algorithm over Z/3Z
Z3_persistence = api.Plex4.getModularSimplicialAlgorithm(3, 3);

% compute and print the intervals
Z3_intervals = Z3_persistence.computeIntervals(stream)