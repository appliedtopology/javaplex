% This script demonstrates an example of the ExplicitSimplexStream class

%% Manual construction

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

% print out the total number of simplices in the complex
size = stream.getSize()

% get the default persistence computation
persistence = api.Plex4.getDefaultSimplicialAlgorithm(3);

% compute and print the intervals
triangle_intervals = persistence.computeIntervals(stream)

%% Example of n-sphere

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

dimension = 9;

% construct simplicial sphere
stream.addElement(0:(dimension + 1));
stream.ensureAllFaces();
stream.removeElementIfPresent(0:(dimension + 1));
stream.finalizeStream();

% print out the total number of simplices in the complex
size = stream.getSize()

% get the default persistence computation
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute and print the intervals
n_sphere_intervals = persistence.computeIntervals(stream)
