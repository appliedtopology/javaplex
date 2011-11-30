clc; clear; close all;

%%
n = 64;
K = 4;

probabilities = linspace(0.1, 0.80, 3);

l = length(probabilities);
generators = cell(l, 1);

for i = 1:l
    generators{i} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, probabilities(i));
end

[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('erdos_renyi_%d_%d_%d', size(generators, 1), n, K);

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);
