% clear variables
clc; close all;

cvx_solver sdpt3

% import data
hom_data;



K = size(homotopies, 1);

% compute initial minimizer
[initial_optimum, chain_map, initial_coefficients] = minimize_variance(cycle_sum, homotopies, domain_vertices, codomain_vertices);

full(chain_map)
dlmwrite('initial.txt', full(chain_map));