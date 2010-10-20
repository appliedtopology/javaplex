clc; close all; clear;

hom_data;

K = homotopies_dimension;
I = codomain_dimension;
J = domain_dimension;

objective_function = @(c) aw_norm(compute_chain_map(c, cycle_sum, homotopies), domain_aw_maps, codomain_aw_maps, domain_vertices, codomain_vertices);
constraint_function = [];
initial_point = randn(K, 1);
A = [];
b = [];
Aeq = [];
beq = [];
lb = -ones(K, 1);
ub = ones(K, 1);
[coefficients, optimum_value] = fmincon(objective_function, initial_point, A, b, Aeq, beq, lb, ub, constraint_function)

chain_map = compute_chain_map(coefficients, cycle_sum, homotopies)