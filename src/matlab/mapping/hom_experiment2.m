best_% clear variables
clc; close all;

cvx_solver sdpt3
%cvx_solver sedumi

% import data
hom_data;

K = size(homotopies, 1);

for i = 1:K
    homotopies{i} = full(homotopies{i});
end

cycle_sum = full(cycle_sum);
candidate_map = cycle_sum;

% compute initial minimizer
[initial_optimum, chain_map, initial_coefficients] = minimize_images(cycle_sum, homotopies);

initial_coefficients
full(chain_map)
dlmwrite('initial.txt', full(chain_map));

repititions = 10;
random_coefficients = zeros(repititions, size(homotopies));
integral_point = [];
point_found = 0;
for i = 1:repititions
    [optimum_value, chain_map, random_coefficients(i, :)] = choose_random_corner_point(cycle_sum, homotopies, initial_optimum);
    rounded_coefficients = round(random_coefficients(i, :));
    rounded_chain_map = compute_chain_map(rounded_coefficients, candidate_map, homotopies);
    if (original_objective(rounded_chain_map) <= initial_optimum)
        integral_point = rounded_chain_map;
        point_found = 1;
        sprintf('Integral Point found!!');
    end
end

%%
samples = 100000;
b = 1;
for i = 1:samples
    convex_v = rand(1, repititions);
    convex_v = convex_v / sum(convex_v);
    coefficients = convex_v * random_coefficients;
    rounded_coefficients = round(coefficients * b) / b;
    rounded_chain_map = compute_chain_map(rounded_coefficients, candidate_map, homotopies);
    if (original_objective(rounded_chain_map) <= initial_optimum)
        integral_point = rounded_chain_map;
        point_found = 1;
        disp(sprintf('Integral point found after %d iterations', i));
        disp(rounded_chain_map);
        dlmwrite('random_corner_point.txt', full(rounded_chain_map));
        break;
    end
end
