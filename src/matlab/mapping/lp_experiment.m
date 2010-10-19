clc; close all; clear;

hom_data;
fval = 2000;
create_lp_data;

[x, fval, exitflag] = linprog(f,A,b,Aeq,beq,lb,ub);

compute_chain_map(x(1:K), cycle_sum, homotopies)
fval

%%
%{
b(num_constraints) = fval + 1e-2;
[x, fval_unused, exitflag] = bintprog(zeros(num_variables, 1),A,b,Aeq,beq);
map = compute_chain_map(x(1:K), cycle_sum, homotopies)
fval
%}
%%

%{
b(num_constraints) = fval + 1e-2;
min_peakiness = 999;

for r = 1:(K * 10)
    v = zeros(num_variables, 1);
    v(1:K) = randn(K, 1);
    %v(r) = -1;
    [x, fval_unused, exitflag] = linprog(v,A,b,Aeq,beq,lb,ub);
    map = compute_chain_map(x(1:K), cycle_sum, homotopies);
    peakiness = max(max(abs(round(map) - map)));
    if (peakiness < min_peakiness)
        min_peakiness = peakiness;
        best_map = map;
    end
    if (peakiness < 1e-2)
        break;
    end
end

best_map = (abs(best_map) > 1e-3) .* best_map

x(1:K)
%}

%%

repititions = K + 2;
corner_coefficients = zeros(repititions, K);
integral_point = [];
point_found = 0;
candidate_map = cycle_sum;
for i = 1:repititions
    v = zeros(num_variables, 1);
    v(1:K) = randn(K, 1);
    [x, fval_unused, exitflag] = linprog(v,A,b,Aeq,beq,lb,ub);
    map = compute_chain_map(x(1:K), cycle_sum, homotopies);
    corner_coefficients(i, :) = x(1:K);
    rounded_coefficients = round(corner_coefficients(i, :));
    rounded_chain_map = compute_chain_map(rounded_coefficients, candidate_map, homotopies);
    if (original_objective(rounded_chain_map) <= fval)
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
    coefficients = convex_v * corner_coefficients;
    rounded_coefficients = round(coefficients * b) / b;
    rounded_chain_map = compute_chain_map(rounded_coefficients, candidate_map, homotopies);
    if (original_objective(rounded_chain_map) <= fval)
        integral_point = rounded_chain_map;
        point_found = 1;
        disp(sprintf('Integral point found after %d iterations', i));
        disp(rounded_chain_map);
        dlmwrite('random_corner_point.txt', full(rounded_chain_map));
        break;
    end
end
