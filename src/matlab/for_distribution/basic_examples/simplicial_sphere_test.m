% This script demonstrates the computation of the homology of a
% high-dimensional sphere that is beyond the reach of the previous version
% of plex.

clc; clear; close all;
import edu.stanford.math.plex4.*;

dimension = 15;

% get the simplicial sphere of the specified dimension
stream = examples.SimplexStreamExamples.getSimplicialSphere(dimension);

% print out the total number of simplices in the complex
size = stream.getSize()

% get the default persistence computation
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)

