clc; clear; close all;

n = 64;
K = 10;
k = 10;

generators = cell(2, 1);

generators{1} = edu.stanford.math.plex4.graph.random.KNearestNeighborsGraph(n, 2, k);
generators{2} = edu.stanford.math.plex4.graph.random.TorusGraph(n, 2, k);

[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('torus_cube_%d_%d_%d_%d', size(generators, 1), n, K, k);

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);