function [optimum_value, chain_map, coefficients] = maximize_peakiness(candidate_map, homotopies, initial_optimum, initial_point)
    n = size(homotopies, 3);
    objective_function = @(c) peakiness(c, candidate_map, homotopies);
    %objective_function = @(c) integral_penalty(c);
    constraint_function = @(c) maximum_constraint(c, candidate_map, homotopies, initial_optimum);
    options = optimset('MaxFunEvals',100000);
    [coefficients, optimum_value] = fmincon(objective_function, initial_point, [], [], [], [], zeros(n, 1), ones(n, 1), constraint_function);
    
    chain_map = compute_chain_map(coefficients, candidate_map, homotopies);