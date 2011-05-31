% This script shows an Euler characteristic example - Section 7.1

clc; clear; close all;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

dimension = 6;

% construct simplicial sphere
stream.addElement(0:(dimension + 1));
stream.ensureAllFaces();
stream.removeElementIfPresent(0:(dimension + 1));
stream.finalizeStream();

eulerCharacteristic(stream, dimension)
