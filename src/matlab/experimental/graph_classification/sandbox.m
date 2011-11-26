clc; clear; close all;

%%
n = 100;
K = 10;

generators = cell(5, 1);

generators{1} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.10);
generators{2} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.15);
generators{3} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.20);
generators{4} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.25);
generators{5} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.30);

pairwise_graph_analysis(generators, K);

%%

n = 200;
K = 10;

generators = cell(2, 1);

generators{1} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.25);
generators{2} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.30);

pairwise_graph_analysis(generators, K);

%%

n = 128;
m = 10;
K = 10;

generators = cell(6, 1);

generators{1} = edu.stanford.math.plex4.graph.random.BAGraph(n, m);
generators{2} = edu.stanford.math.plex4.graph.random.BinaryHierarchicalGraph(n, 0.6);
generators{3} = edu.stanford.math.plex4.graph.random.ErdosRenyiGraph(n, 0.30);

generators{4} = edu.stanford.math.plex4.graph.random.RandomGeometricGraph(n, 1, 0.02);
generators{5} = edu.stanford.math.plex4.graph.random.RandomGeometricGraph(n, 2, 0.02);
generators{6} = edu.stanford.math.plex4.graph.random.RandomGeometricGraph(n, 3, 0.02);

pairwise_graph_analysis(generators, K);
