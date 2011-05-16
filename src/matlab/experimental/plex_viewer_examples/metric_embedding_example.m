
clc; clear; close all;

stream = edu.stanford.math.plex4.examples.SimplexStreamExamples.getOctahedron();
graph = edu.stanford.math.plex4.streams.utility.StreamUtility.getNeighborhoodGraph(stream);
distance_matrix = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
laplacian_matrix = edu.stanford.math.plex4.graph.utility.GraphUtility.getLaplacianMatrix(graph);

distance_matrix = min(distance_matrix, 5);

[Y, eigvals] = cmdscale(distance_matrix)
PlexViewer.drawSimplexStream(stream, Y(:, 1:3));

%[Y, E] = eig(double(laplacian_matrix));
%PlexViewer.drawSimplexStream(stream, Y(:, 2:4));
