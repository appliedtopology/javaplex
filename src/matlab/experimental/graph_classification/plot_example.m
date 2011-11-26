clc; clear; close all;

n = 100;

p = 0.6;
q = 0.02;

c = 4;

P = diag((p - q) * ones(1, c)) + q * ones(c, c)

%%
generator = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);

graph = generator.generate();
distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
adjacency = edu.stanford.math.plex4.graph.utility.GraphUtility.getAdjacencyMatrix(graph);
[Y] = cmdscale(distances);

points = [Y(:, 1) Y(:, 2), Y(:, 3)];

%%
stream = edu.stanford.math.plex4.graph.utility.GraphUtility.toSimplexStream(graph)

%% Output

%render_onscreen(stream, points);

%%
filename = 'blockmodel_graph.pov';
create_pov_file(stream, points, filename);
render_pov_file(filename);