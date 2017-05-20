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

% compute boundary
d_1 = streams.utility.StreamUtility.createBoundaryMatrixAsDoubleSum(stream, 1)

% convert from sparse form to array
converter = api.Plex4.createHomMatrixConverter(stream, stream);
d_1_array = converter.toArray(d_1)
