% This script computes various cellular homology examples

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% Cellular Sphere

dimension = 100;

% get the cellular sphere of the specified dimension
stream = examples.CellStreamExamples.getCellularSphere(dimension);

% get the total number of simplices in the complex
size = stream.getSize();

Z2_persistence = api.Plex4.getModularCellularAlgorithm(dimension + 1, 2);

% compute and print the intervals
intervals = Z2_persistence.computeIntervals(stream)

%% Klein Bottle

% get the cellular sphere of the specified dimension
stream = examples.CellStreamExamples.getCellularKleinBottle();

% get the total number of simplices in the complex
size = stream.getSize();

% get cellular homology algorithm over Z/2Z
Z2_persistence = api.Plex4.getModularCellularAlgorithm(dimension + 1, 2);
% get cellular homology algorithm over Z/3Z
Z3_persistence = api.Plex4.getModularCellularAlgorithm(dimension + 1, 3);
% get cellular homology algorithm over Q
Q_persistence = api.Plex4.getRationalCellularAlgorithm(dimension + 1);

% compute over Z/2Z - should give (1, 2, 1)
Z2_intervals = Z2_persistence.computeIntervals(stream)

% compute over Z/3Z - should give (1, 1, 0)
Z3_intervals = Z3_persistence.computeIntervals(stream)

% compute over Q - should give (1, 1, 0)
Q_intervals = Q_persistence.computeIntervals(stream)

