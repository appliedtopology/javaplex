
clc; clear; close all;

domain_size = 4;
codomain_size = 60;

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
codomain_points = codomain_points + 0 * ones(codomain_size, 1) * [3, 0];

domain_theta = ([0:domain_size - 1] * (2 * pi / domain_size))';
codomain_theta = ([0:codomain_size - 1] * (2 * pi / codomain_size))';
%}


utility.RandomUtility.initializeWithSeed(0);
hold on;
codomain_points = examples.PointCloudExamples.getRandomTrefoilKnotPoints(codomain_size);
%codomain_points = examples.PointCloudExamples.getRandomSpherePoints(codomain_size, 1);
plot3(codomain_points(:, 1), codomain_points(:, 2), codomain_points(:, 3), 'bx') + randn() * 0.1;
%scatter(codomain_points(:, 1), codomain_points(:, 2), 'bx');
domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
%domain_points = domain_points + ones(domain_size, 1) * [3, 0];

% create a randomized landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(codomain_points, codomain_size / 2);
% create a Lazy-Witness Stream - note that this sets the number of
% divisions to the default value of 20
codomain_stream = api.Plex4.createLazyWitnessStream(landmark_selector, 2, 0.6, 5);
codomain_stream.finalizeStream();
codomain_points = codomain_points(landmark_selector.getLandmarkPoints()+1, :);

domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
domain_stream.finalizeStream();


% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(2);

% compute the intervals and transform them to filtration values
domain_index_intervals = persistence.computeIntervals(domain_stream)

% compute the intervals and transform them to filtration values
codomain_index_intervals = persistence.computeIntervals(codomain_stream)
%%
% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

%%

sigma = 0.5;

K = size(homotopies, 1);
edge_list = get_edges(domain_stream);
distance_function = @(a, b) circle_distance(a, b);
objective = @(c) density_objective(compute_mapping(cycle_sum, homotopies, c(1:K)), domain_points, codomain_points, gaussian_kernel_densities(codomain_points, codomain_points, sigma));

%c_0 = randn(K, 1);

[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval
% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies, (fval * 1.1));
%c_0 = randn(size(lb, 1), 1);
c_0 = randn(K, 1) * 0.1;

options.MaxFunEvals = 100000;
options.MaxIter = 100000;
[x, fval, exitflag, output] = fminunc(objective, c_0, options);
%[x, fval, exitflag, output] = fminsearch(objective, c_0, options);
%[x, fval, exitflag, output] = fmincon(objective, c_0, A, b, Aeq, beq, lb, ub, [], options);
%[x, fval, exitflag, output] = fmincon(objective, c_0, [], [], [], [], lb, ub, [], options);
map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map

%%

%interpolated_points = compute_interpolated_points((map), domain_points, codomain_points);
interpolated_points = [codomain_points(30,:); codomain_points(18,:); codomain_points(3,:)] - rand() * 0.02;
%figure;
%plot3(codomain_points(:, 1), codomain_points(:, 2), codomain_points(:, 3), 'bo');
hold on;
plot3(interpolated_points(:, 1), interpolated_points(:, 2), interpolated_points(:, 3), 'ro');
grid off;
legend('Codomain Points', 'Domain Points');
%dlmwrite('matrix.txt', full(map));
%figure;
%scatter(domain_theta, mod(interpolated_theta, 2 * pi));
%map = normalize_rows(map);


%PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
%PlexViewer.drawMapping(codomain_stream, codomain_points, domain_stream, domain_points, abs(map'));
