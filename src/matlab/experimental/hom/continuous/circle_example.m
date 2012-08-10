%% Continuous circle example - finds random corner point of feasible set

clc; clear; close all;

domain_size = 4;
codomain_size = 5;

domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

% setup the LP - find a feasible point and the value of the max function in
% the set C
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map;

full(map)

% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies, fval, randn(size(homotopies, 1), 1));
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map;

full(map)

dlmwrite('matrix.txt', full(map));

%%

import edu.stanford.math.plex_viewer.*;
Api.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points + ones(codomain_size, 2), abs(map));