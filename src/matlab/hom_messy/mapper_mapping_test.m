
clc; clear; close all;

domain_size = 300;
codomain_size = 300;

% create the domain and codomain simplicial complexes
%utility.RandomUtility.initializeWithSeed(0);
%domain_points = examples.PointCloudExamples.getGaussianPoints(domain_size, 2) * [1 0; 0 0.7];
%codomain_points = examples.PointCloudExamples.getGaussianPoints(codomain_size, 2) * [0.2 0; 0 2];
domain_points = rand(domain_size, 2) * [1 0; 0 0.2];
codomain_points = rand(domain_size, 2) * [0.2 0; 0 2];
[domain_stream, domain_filter, domain_adjacency_matrix] = create_quotient_mapper_complex(domain_points);
[codomain_stream, codomain_filter, codomain_adjacency_matrix] = create_quotient_mapper_complex(codomain_points);

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
[f, A, b, Aeq, beq, lb, ub] = create_positive_lp(cycle_sum, homotopies);
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);
fval
% find a random corner point of the set of optima
[f, A, b, Aeq, beq, lb, ub] = create_positive_lp(cycle_sum, homotopies, fval, randn(size(homotopies, 1), 1));
[x, fval, exitflag, output, lambda] = linprog(f, A, b, Aeq, beq, lb, ub);

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)));
map = (abs(map) > 1e-3) .* map
%%
%map = normalize_rows(map);
%map = normalize_rows(map')';
%%

domain_graph = to_graph(domain_stream);
codomain_graph = to_graph(codomain_stream);

dlmwrite('mapping.txt', full(map));
dlmwrite('domain_points.txt', graph_embedding(domain_graph, 2));
dlmwrite('codomain_points.txt', graph_embedding(codomain_graph, 2));
stream_reader_writer = io.SimplexStreamReaderWriter.getInstance();
stream_reader_writer.writeToFile(domain_stream, 'domain_stream.txt');
stream_reader_writer.writeToFile(codomain_stream, 'codomain_stream.txt');

%PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, (map));

%figure;
%subplot(2, 1, 1);
%%
figure;
gplot(domain_graph, graph_embedding(domain_graph, 2), '-*');
figure;
gplot(codomain_graph, graph_embedding(codomain_graph, 2), '-*');
