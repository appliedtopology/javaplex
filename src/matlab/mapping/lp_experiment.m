clc; close all; clear;
tic()
hom_data;
fval = 2000;
create_lp_data;

[x,fval,exitflag,output,lambda] = linprog(f,A,b,Aeq,beq,lb,ub);

map = compute_chain_map(x(1:K), cycle_sum, homotopies)
fval
b(num_constraints) = fval;
toc()

total_sum = sum(sum(abs(map)))
%col_sums = sum(abs(map))
%row_sums = sum(abs(map'))
map = diag(1.0 ./ sqrt(sum((map.^2)'))) * map;
dlmwrite('corner_point.txt', full(map));

%%
%{
tic()

H = speye(num_variables);
v = zeros(num_variables, 1);
[x,fval,exitflag,output,lambda] = quadprog(H,v,A,b,Aeq,beq,lb,ub);

map = compute_chain_map(x(1:K), cycle_sum, homotopies)
fval
b(num_constraints) = fval;
toc()
%}
%%
%{
tic()
objective_function = @(c) aw_norm(compute_chain_map(c, cycle_sum, homotopies), domain_aw_maps, codomain_aw_maps, domain_vertices, codomain_vertices);
constraint_function = @(c) maximum_constraint(c, cycle_sum, homotopies, fval);
initial_point = randn(K, 1);
%initial_point = randn(K, 1);
A = [];
b = [];
Aeq = [];
beq = [];
lb = -ones(K, 1);
ub = ones(K, 1);
[coefficients, optimum_value] = fmincon(objective_function, initial_point, A, b, Aeq, beq, lb, ub, constraint_function);

chain_map = compute_chain_map(coefficients, cycle_sum, homotopies)
optimum_value
toc()
%}
%%
%{
tic()
%b(num_constraints) = 2000;
v = randn(num_variables, 1);
v(1:K) = randn(K, 1);
%v(r) = -1;
f = zeros(num_variables, 1);
for i = 1:I*J
    f(i + K) = 1;
end
[x,fval,exitflag,output,lambda] = linprog(f,A,b,Aeq,beq,lb,ub);
map = compute_chain_map(x(1:K), cycle_sum, homotopies);
map = (abs(map) > 1e-3) .* map
toc()
dlmwrite('corner_point.txt', full(map));
total_sum = sum(sum(abs(map)))
%col_sums = sum(abs(map))
%row_sums = sum(abs(map'))
%lambda
%x;
%}
%%
%%
%{
tic()
[x, fval_unused, exitflag] = bintprog(zeros(num_variables, 1),A,b,Aeq,beq);
map = compute_chain_map(x(1:K), cycle_sum, homotopies)
fval
toc()
dlmwrite('corner_point.txt', full(map));
sum(sum(abs(map)))
%}
%%

%b(num_constraints) = fval + 1e-2;
max_peakiness = -9999;

repititions = 100;
maps = cell(repititions, 1);
x = cell(repititions, 1);
matlabpool

parfor i = 1:repititions
    v = randn(num_variables, 1);
    [x{i}, fval_unused, exitflag] = linprog(v,A,b,Aeq,beq,lb,ub);
    maps{i} = compute_chain_map(x{i}(1:K), cycle_sum, homotopies);
end

matlabpool close

for i = 1:repititions
    map = maps{i};
    peakiness = sum(sum(map.^2)) / sum(sum(abs(map)));
    if (peakiness > max_peakiness)
        max_peakiness = peakiness;
        best_map = map;
    end
end

best_map = (abs(best_map) > 1e-3) .* best_map

dlmwrite('corner_point.txt', full(best_map));
%sums = sum(abs(best_map))
total_sum = sum(sum(abs(map)))



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
%{
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
samples = 10000;
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
%}

