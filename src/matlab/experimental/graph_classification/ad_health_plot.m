clc; clear; close all;

%%
edges = get_adhealth_edges47();

query_graph = create_graph_from_edgelist(edges);


options.filename = 'ad_health_graph_47.pov';
plot_graph(query_graph, options);
