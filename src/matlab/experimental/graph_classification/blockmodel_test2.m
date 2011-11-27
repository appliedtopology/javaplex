clc; clear; close all;

%%
n = 64;
p = 0.4;
q = 0.02;
K = 10;
c_min = 1;
c_max = 3;
C = c_max - c_min + 1;

generators = cell(C, 1);

for i = 1:C
    c = i + c_min - 1;
    P = diag((p - q) * ones(1, c)) + q * ones(c, c);
    generators{i} = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);
end


[bottleneck_distances] = pairwise_graph_analysis(generators, K);

%%

label = sprintf('blockmodels2_%d_%d_%d_%d', c_min, c_max, n, K);

[hm_handle, mds_handle] = visualize_dissimilarity_matrix(bottleneck_distances, generators, label);