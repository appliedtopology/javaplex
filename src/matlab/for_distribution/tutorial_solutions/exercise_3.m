% Exercise 3

% We use 9 vertices, which we think of as a 3x3 grid numbered as a 
% telephone keypad. We identify opposite sides, with left and right sides 
% identified with a twist. For a picture, see 
% Appendix B of the tutorial.

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% add simplices
stream.addElement([1, 2, 7]);
stream.addElement([2, 7, 8]);
stream.addElement([2, 3, 8]);
stream.addElement([3, 8, 9]);
stream.addElement([1, 3, 9]);
stream.addElement([1, 4, 9]);
stream.addElement([4, 7, 8]);
stream.addElement([4, 5, 8]);
stream.addElement([5, 8, 9]);
stream.addElement([5, 6, 9]);
stream.addElement([6, 4, 9]);
stream.addElement([4, 6, 7]);

stream.addElement([1, 4, 5]);
stream.addElement([1, 2, 5]);
stream.addElement([2, 5, 6]);
stream.addElement([2, 3, 6]);
stream.addElement([3, 6, 7]);
stream.addElement([1, 3, 7]);

stream.ensureAllFaces();

stream.finalizeStream();

% get persistence algorithm over Z/2Z
Z2_persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
Z2_intervals = Z2_persistence.computeIntervals(stream)

% get persistence  algorithm over Z/3Z
Z3_persistence = api.Plex4.getModularSimplicialAlgorithm(3, 3);

% compute and print the intervals
Z3_intervals = Z3_persistence.computeIntervals(stream)