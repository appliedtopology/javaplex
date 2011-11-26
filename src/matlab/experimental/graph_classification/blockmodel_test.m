clc; clear; close all;

%%
n = 64;
K = 4;

p_max = 0.6;
p_min = 0.1;


probabilities = linspace(p_min, p_max, 11);

l = length(probabilities);
generators = cell(l, 1);

for i = 1:l
    p = p_max;
    q = probabilities(i);
    P = [p, q; q, p];
    generators{i} = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);
end

[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('blockmodels_%d_%d_%d', size(generators, 1), n, K);

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);