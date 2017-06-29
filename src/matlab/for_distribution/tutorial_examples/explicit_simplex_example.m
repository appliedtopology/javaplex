% This script demonstrates the use of the explicit simplex streams -
% Section 3.1

%% Circle Example

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% construct a triangle
stream.addVertex(0);
stream.addVertex(1);
stream.addVertex(2);
stream.addElement([0, 1]);
stream.addElement([0, 2]);
stream.addElement([1, 2]);
stream.finalizeStream();

% print out the total number of simplices in the complex
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)

% compute and print the intervals annotated with a representative cycle
intervals = persistence.computeAnnotatedIntervals(stream)

%% 9-sphere Example

dimension = 9;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% construct simplicial sphere
stream.addElement(0:(dimension + 1));
stream.ensureAllFaces();
stream.removeElementIfPresent(0:(dimension + 1));
stream.finalizeStream();

% print out the total number of simplices in the complex
num_simplices = stream.getSize()

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(dimension + 1, 2);

% compute and print the intervals
intervals = persistence.computeIntervals(stream)

% compute and print the intervals annotated with a representative cycle
intervals = persistence.computeAnnotatedIntervals(stream)
