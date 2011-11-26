clc; clear; close all;

n = 100;
p = 0.4;
q = 0.02;
c = 2;
P = diag((p - q) * ones(1, c)) + q * ones(c, c);

%%
generator = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);

graph = generator.generate();
distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
distances = filter_distance_matrix(distances);
adjacency = edu.stanford.math.plex4.graph.utility.GraphUtility.getAdjacencyMatrix(graph);
laplacian = edu.stanford.math.plex4.graph.utility.GraphUtility.getLaplacianMatrix(graph);
normalized_laplacian = compute_normalized_laplacian(double(laplacian));

%{
%[Y] = spectral_embedding(graph, 3);
%[Y, D] = eig(double(laplacian));
%points = [Y(1, :)', Y(2, :)'];
%}

[Y] = cmdscale(distances);
points = [Y(:, 1) Y(:, 2), Y(:, 3)];

%% Convert graph to simplex stream
stream = edu.stanford.math.plex4.graph.utility.GraphUtility.toSimplexStream(graph);


%% Produce Raytraced plot

filename = 'blockmodel_graph.pov';
create_pov_file(stream, points, filename);
render_pov_file(filename);

%% Render On-screen

render_onscreen(stream, points);
