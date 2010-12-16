
clc; clear; close all;

domain_size = 10;
codomain_size = 4;

% create the domain and codomain simplicial complexes

%{
width = 4;
length = 8;
domain_stream = examples.SimplexStreamExamples.getAnnulus(width, length);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getAnnulusVertices(width, length);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);

%}

%{
domain_stream = examples.SimplexStreamExamples.getIcosahedron();
codomain_stream = examples.SimplexStreamExamples.getOctahedron();

domain_points = examples.PointCloudExamples.getIcosahedronVertices();
codomain_points = examples.PointCloudExamples.getOctahedronVertices() + 1;
%}

%{
domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);
codomain_points = codomain_points + ones(codomain_size, 1) * [3, 0];

domain_theta = ([0:domain_size - 1] * (2 * pi / domain_size))';
codomain_theta = ([0:codomain_size - 1] * (2 * pi / codomain_size))';

%}

utility.RandomUtility.initializeWithSeed(0);

domain_points = examples.PointCloudExamples.getRandomTrefoilKnotPoints(domain_size);
plot3(domain_points(:, 1), domain_points(:, 2), domain_points(:, 3), 'bx');
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size);
%domain_points = domain_points + ones(domain_size, 1) * [3, 0];

% create a randomized landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(domain_points, 10);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
domain_stream = api.Plex4.createLazyWitnessStream(landmark_selector, 2, 0.5, 20);
domain_stream.finalizeStream();
domain_points = domain_points(landmark_selector.getLandmarkPoints()+1, :);

codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);
codomain_stream.finalizeStream();

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(2);

% compute the intervals and transform them to filtration values
domain_index_intervals = persistence.computeIntervals(domain_stream)

% compute the intervals and transform them to filtration values
codomain_index_intervals = persistence.computeIntervals(codomain_stream)
%%
% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

% setup the LP - find a feasible point and the value of the max function in
% the set C
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval
% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies, fval, randn(size(homotopies, 1), 1));
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map
%%
map = normalize_rows(map);
%map = normalize_rows(map')';
%%
dlmwrite('matrix.txt', full(map));

%%
%interpolated_theta = compute_interpolated_points(map, domain_points, codomain_theta);

%interpolated_points = [cos(interpolated_theta), sin(interpolated_theta)];
%codomain_points = [cos(codomain_theta), sin(codomain_theta)];

%scatter(codomain_points(:, 1), codomain_points(:, 2), 'bo');
%hold on;
%scatter(interpolated_points(:, 1), interpolated_points(:, 2), 'rx');

%figure;
%scatter(domain_theta, interpolated_theta);
%map = normalize_rows(map);

PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
%PlexViewer.drawMapping(codomain_stream, codomain_points, domain_stream, domain_points, abs(map'));
