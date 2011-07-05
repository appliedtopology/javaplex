clc; clear; close all;

domain_size = 8;
codomain_size = 8;

domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);

%%
% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);
K = size(homotopies, 1);

cost_function = @(x) default_objective(x, cycle_sum, homotopies);
random_neighbor_function = @(x) flip_random_entry(x);
initial_point = zeros(1, K);

[optimizer, optimum] = simulated_annealing(cost_function, random_neighbor_function, initial_point);

%%
map = full(compute_mapping(cycle_sum, homotopies, optimizer))

optimum