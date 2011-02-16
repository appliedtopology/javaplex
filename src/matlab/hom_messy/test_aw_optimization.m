
clc; clear; close all;

domain_size = 5;
codomain_size = 4;

% create the domain and codomain simplicial complexes
%{
domain_stream = examples.SimplexStreamExamples.getTetrahedron();
codomain_stream = examples.SimplexStreamExamples.getOctahedron();

domain_points = examples.PointCloudExamples.getTetrahedronVertices();
codomain_points = examples.PointCloudExamples.getOctahedronVertices() + 1;
%}


domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);
codomain_points = codomain_points + ones(codomain_size, 1) * [3, 0];


% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

domain_aw_matrix = convert_sparse_matrix(streams.utility.StreamUtility.createAlexanderWhitneyMatrix(domain_stream));
codomain_aw_matrix = convert_sparse_matrix(streams.utility.StreamUtility.createAlexanderWhitneyMatrix(codomain_stream));

[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval
% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies, ceil(fval));

K = size(homotopies, 1);

loss_function = @(x) sum(x.^2);
%loss_function = @(x) sum(abs(x));

objective = @(c) alexander_whitney_loss_function(compute_mapping(cycle_sum, homotopies, c(1:K)), domain_aw_matrix, codomain_aw_matrix, loss_function);
c_0 = randn(size(lb, 1), 1);

[x, fval, exitflag, output] = fminunc(objective, c_0);
%[x, fval, exitflag, output] = fmincon(objective, c_0, A, b, Aeq, beq, lb, ub);

%{
opts.tol = 1e-6;
opts.maxIts = 500;
opts.alg = 'N07';
x = tfocs(objectivize(objective), [], [], c_0, opts)
%}
%%
map = compute_mapping(cycle_sum, homotopies, x(1:K));
map = (abs(map) > 1e-2) .* map

map = normalize_rows(map);

dlmwrite('matrix.txt', map');

%%
%PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
