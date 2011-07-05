%% Brute-force discrete hom example

% Note that this is obviously impractical for anything other than the
% tiniest examples. This is just designed to enumerate the set of minima of
% the bisimpliciality objective for small cases.

clc; clear; close all;

domain_size = 4;
codomain_size = 4;

domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);

%% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);
K = size(homotopies, 1);

initial_chain_map = full(cycle_sum)

for i=1:K
    full(homotopies{i});
end

%% Compute objective for all coefficients

subsets = get_binary_subsets(K);
L = size(subsets, 1);
values = zeros(L, 1);

for i = 1:L
    values(i) = default_objective(subsets(i, :), cycle_sum, homotopies);
end

search_space_size = L

%% Plot values
plot(values, 'bo');
ylabel('Simpliciality penalty');
xlabel('Binary representation of coefficient set');

%% Find minima
min_value = min(values);
[r, c, v] = find(values == min_value);

num_minimizers = length(r)

for i = 1:length(r)
    subsets(r(i), :);
end