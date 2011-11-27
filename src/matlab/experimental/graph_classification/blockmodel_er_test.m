clc; clear; close all;

n = 64;
K = 2;

p = 0.6;
q = 0.1;

P = [p, q; q, p];

Q = [0.8, 0.4; 0.4, 0.8];

generators = cell(3, 1);

generators{1} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, p);
generators{2} = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);
generators{3} = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, Q);


[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('blockmodel_%d_%d_%d', size(generators, 1), n, K);

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);