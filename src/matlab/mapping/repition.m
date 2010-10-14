% clear variables
clc; close all;

% import data
hom_data;

K = size(homotopies, 1);

% compute initial minimizer
[initial_optimum, chain_map, initial_coefficients] = minimize_images(cycle_sum, homotopies);

repititions = 40;

max_p = -1;
best_map = 0;
fn = @(Q) sum(sum(Q.^2)) / sum(sum(abs(Q)));

for i = 1:repititions

    % find random corner point within the space of minimizers
    [optimum_value, chain_map, random_coefficients] = choose_random_corner_point(cycle_sum, homotopies, initial_optimum);
    
    p = fn(chain_map);

    if (p > max_p)
        max_p = p
        best_map = chain_map;
    end
    
    rounded_coefficients = round(random_coefficients);
    rounded_map = compute_chain_map(rounded_coefficients, cycle_sum, homotopies);
    if (original_objective(rounded_map) <= initial_optimum + 1e-4)
        integral_solution = rounded_coefficients;
    end
end

full(best_map)
dlmwrite('best.txt', full(best_map))