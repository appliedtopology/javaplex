%% This script uses greedy search to attempt to minimize the bisimpliciality objective

clc; clear; close all;

domain_size = 5;
codomain_size = 5;

domain_stream = examples.SimplexStreamExamples.getTorus();
codomain_stream = examples.SimplexStreamExamples.getTorus();

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);
K = size(homotopies, 1);

cost_function = @(x) default_objective(x, cycle_sum, homotopies);
initial_point = round(randn(1, K));

[optimizer, optimum] = greedy_search(cost_function, initial_point);

%%
map = full(compute_mapping(cycle_sum, homotopies, optimizer))

optimum