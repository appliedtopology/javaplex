% This script shows an Euler characteristic example - Section 7.1

clc; clear; close all;
import edu.stanford.math.plex4.*;

dimension = 6;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream();

% construct simplicial sphere
stream.addElement(0:(dimension + 1));
stream.ensureAllFaces();
stream.removeElementIfPresent(0:(dimension + 1));
stream.finalizeStream();

eulerCharacteristic(stream, dimension)
