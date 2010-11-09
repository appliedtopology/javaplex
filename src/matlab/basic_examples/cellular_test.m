% This script computes various cellular homology examples

clc; clear; close all;

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

Z2_persistence = api.Plex4.getModularCellularAlgorithm(dimension + 1, 2);
Z3_persistence = api.Plex4.getModularCellularAlgorithm(dimension + 1, 3);
Q_persistence = api.Plex4.getRationalCellularAlgorithm(dimension + 1);

% compute over Z_2 - should give (1, 2, 1)
Z2_intervals = Z2_persistence.computeIntervals(stream)

% compute over Z_3 - should give (1, 1, 0)
Z3_intervals = Z3_persistence.computeIntervals(stream)

% compute over Q - should give (1, 1, 0)
Q_intervals = Q_persistence.computeIntervals(stream)

