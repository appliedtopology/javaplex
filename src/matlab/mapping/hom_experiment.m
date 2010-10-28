% clear variables
clc; close all;

cvx_solver sdpt3

% import data
hom_data;



K = size(homotopies, 1);

% compute initial minimizer
[initial_optimum, chain_map, initial_coefficients] = minimize_images(cycle_sum, homotopies);

full(chain_map)
dlmwrite('initial.txt', full(chain_map));
sum(sum(abs(chain_map)))

%choose l1 minimizer within space of minimizers
[optimum_value, chain_map, coefficients] = choose_sparse_point(cycle_sum, homotopies, initial_optimum);

full(chain_map)
dlmwrite('corner_point.txt', full(chain_map));


% find random corner point within the space of minimizers
%[optimum_value, chain_map, random_coefficients] = choose_random_corner_point(cycle_sum, homotopies, initial_optimum);

%full(chain_map)
%dlmwrite('random_corner_point.txt', full(chain_map));

aw_norm(chain_map, domain_aw_maps, codomain_aw_maps)

%{
% choose l1 minimizer within space of minimizers
[optimum_value, chain_map, coefficients] = choose_sparse_point(cycle_sum, homotopies, initial_optimum);

full(chain_map)
dlmwrite('sparse_point.txt', full(chain_map));

[optimum_value, chain_map, coefficients] = maximize_peakiness(cycle_sum, homotopies, initial_optimum, 0 * rand(K, 1) + random_coefficients)

full(chain_map)
dlmwrite('peaky.txt', full(chain_map));

%dlmwrite('map.txt', full(chain_map));

%coefficients = round(coefficients);

full(compute_chain_map(coefficients, cycle_sum, homotopies))

[initial_optimum, chain_map, initial_coefficients] = minimize_weighted_images(cycle_sum, homotopies, create_weight_matrix(codomain_dimension, codomain_vertices));

full(chain_map)
dlmwrite('weighted_min.txt', full(chain_map));

[initial_optimum, chain_map, initial_coefficients] = minimize_l1(cycle_sum, homotopies);

[optimum_value, chain_map, coefficients] = choose_sparse_point(cycle_sum, homotopies, initial_optimum);

full(chain_map)

% find random corner point within the space of minimizers
%[optimum_value, chain_map, random_coefficients] = choose_random_corner_point(cycle_sum, homotopies, initial_optimum);

%full(chain_map)

%compute_chain_map(coefficients, cycle_sum, homotopies)

%}
