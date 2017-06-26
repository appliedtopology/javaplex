% Exercise 2

% We use 9 vertices, which we think of as a 3x3 grid numbered as a 
% telephone keypad. We identify opposite sides. For a picture, see 
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
stream.addElement([1, 7, 9]);
stream.addElement([4, 7, 8]);
stream.addElement([4, 5, 8]);
stream.addElement([5, 8, 9]);
stream.addElement([5, 6, 9]);
stream.addElement([6, 7, 9]);
stream.addElement([4, 6, 7]);

stream.addElement([1, 4, 5]);
stream.addElement([1, 2, 5]);
stream.addElement([2, 5, 6]);
stream.addElement([2, 3, 6]);
stream.addElement([3, 6, 4]);
stream.addElement([1, 3, 4]);

stream.ensureAllFaces();

stream.finalizeStream();

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)
