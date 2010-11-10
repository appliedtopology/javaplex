
clc; clear; close all;

domain_size = 30;
codomain_size = 3;

% create the domain and codomain simplicial complexes
%{
domain_stream = examples.SimplexStreamExamples.getTetrahedron();
codomain_stream = examples.SimplexStreamExamples.getOctahedron();

domain_points = examples.PointCloudExamples.getTetrahedronVertices();
codomain_points = examples.PointCloudExamples.getOctahedronVertices() + 1;
%}

%{
domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);
codomain_points = codomain_points + ones(codomain_size, 1) * [3, 0];
%}

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size) + randn(domain_size, 2) * 0.03;
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);
domain_points = domain_points + ones(domain_size, 1) * [3, 0];

% create a randomized landmark selector
landmark_selector = api.Plex4.createRandomSelector(domain_points, domain_size / 2);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
domain_stream = api.Plex4.createLazyWitnessStream(landmark_selector, 2, 0.4);
domain_points = domain_points(landmark_selector.getLandmarkPoints()+1, :);

codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(2);

% compute the intervals and transform them to filtration values
domain_index_intervals = persistence.computeIntervals(domain_stream)

% compute the intervals and transform them to filtration values
codomain_index_intervals = persistence.computeIntervals(codomain_stream)

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

% setup the LP - find a feasible point and the value of the max function in
% the set C
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);

% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies, fval, randn(size(homotopies, 1), 1));
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map;

%map = normalize_rows(map);

%PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
PlexViewer.drawMapping(codomain_stream, codomain_points, domain_stream, domain_points, abs(map'));