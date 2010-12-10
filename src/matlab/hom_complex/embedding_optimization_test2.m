
clc; clear; close all;

domain_size = 4;
codomain_size = 3;

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

domain_theta = ([0:domain_size - 1] * (2 * pi / domain_size))';
codomain_theta = ([0:codomain_size - 1] * (2 * pi / codomain_size))';

%{
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
%}

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(2);

% compute the intervals and transform them to filtration values
domain_index_intervals = persistence.computeIntervals(domain_stream)

% compute the intervals and transform them to filtration values
codomain_index_intervals = persistence.computeIntervals(codomain_stream)

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

%%
K = size(homotopies, 1);
[J, I] = size(cycle_sum);
v = randn(K, 1);

cvx_begin
    variable c(K);
    variable G(J, I);
    minimize (v' * c);
    subject to
        G == compute_mapping(cycle_sum, homotopies, c);
        norm(G) <= 1.8257;
        c <= 1;
        c >= -1;
cvx_end

map = G

cvx_optval

%%
interpolated_theta = compute_interpolated_points(map, domain_points, codomain_theta);

interpolated_points = [cos(interpolated_theta), sin(interpolated_theta)];
codomain_points = [cos(codomain_theta), sin(codomain_theta)];

scatter(codomain_points(:, 1), codomain_points(:, 2), 'bo');
hold on;
scatter(interpolated_points(:, 1), interpolated_points(:, 2), 'rx');

figure;
scatter(domain_theta, interpolated_theta);
%map = normalize_rows(map);

%PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, abs(map));
%PlexViewer.drawMapping(codomain_stream, codomain_points, domain_stream, domain_points, abs(map'));
