clc; clear; close all;

n = 1000;
d = 2;
r = 0.05;

[stream, points] = random_geometric_graph(n, d, r);

points = (points - 0.5) * 2;

%%
filename = 'random_geometric_graph.pov';
create_pov_file(stream, points, filename);
render_pov_file(filename);