
clc; clear; close all;

domain_size = 10;
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


loss_function = @(x) sum(x.^2);
%loss_function = @(x) sum(abs(x));

objective = @(c) alexander_whitney_loss_function(compute_mapping(cycle_sum, homotopies, c), domain_aw_matrix, codomain_aw_matrix, loss_function);
c_0 = randn(size(homotopies, 1), 1);

[x, fval, exitflag, output] = fminunc(objective, c_0);
%{
opts.tol = 1e-6;
opts.maxIts = 500;
opts.alg = 'N07';
x = tfocs(objectivize(objective), [], [], c_0, opts)
%}

map = compute_mapping(cycle_sum, homotopies, x);
map = (abs(map) > 1e-2) .* map

map = normalize_rows(map);
%%
PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
