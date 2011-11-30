clc; clear; close all;

%%
K = 2;

sizes = 20:10:90;

%probabilities = 5.0 ./ sizes;
probabilities = 0.1 * ones(length(sizes), 1);

l = length(probabilities);
generators = cell(l, 1);

for i = 1:l
    generators{i} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(sizes(i), probabilities(i));
end

[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('erdos_renyi_increasing_%d_%d_%d_%d', size(generators, 1), K, min(sizes), max(sizes));

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);
