clc; clear; close all;

%%
edges = get_adhealth_edges47();

query_graph = create_graph_from_edgelist(edges);

num_vertices = query_graph.getNumVertices();
num_edges = size(edges, 1) / 2;
estimated_p = 2 * num_edges / (num_vertices * (num_vertices - 1))

%plot_graph(graph)

%%

n = query_graph.getNumVertices();
%n = 1000;

lambda = 0.05;
K = 10;
c_min = 1;
c_max = 4;


C = c_max - c_min + 1;

generators = cell(C, 1);

for i = 1:C
    c = i + c_min - 1;
    m = num_vertices / c;
    
    p = num_edges / (0.5 * m * (m-1) * c + 0.5 * lambda * m * m * c * (c - 1));
    q = p * lambda;
    
    p = p * 1;
    q = q * 1;
    
    P = diag((p - q) * ones(1, c)) + q * ones(c, c);
    generators{i} = edu.stanford.math.plex4.graph.random.StochasticBlockmodel(n, P);
end

query_graphs = cell(1, 1);
query_graphs{1} = query_graph;

%query_graphs{1} = generators{1}.generate();
%query_graphs{2} = generators{1}.generate();

distances = comparative_graph_analysis(generators, query_graphs, K)

%%

averaged_distances = average_block_matrix(distances, size(generators, 1), size(query_graphs, 1));

label = sprintf('adhealth_47_%d_%d', c_min, c_max);
query_labels = cell(1, 1);
query_labels{1} = 'adhealth_47';
visualize_comparison_distances(averaged_distances, generators, query_labels, label);