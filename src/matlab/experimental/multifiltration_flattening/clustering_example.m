clc; clear; close all;

%% Load data

load Syn2d_5g1k3c
points = dataM_c1;

% mean center
points(:, 1) = points(:, 1) - mean(points(:, 1));
points(:, 2) = points(:, 2) - mean(points(:, 2));
%scatter(points(:, 1), points(:, 2));

%% Parameters

max_dimension = 0;
max_filtration_value = 2;
sigma = 1;
principal_direction = [0, 0.01];

metric_space = metric.impl.EuclideanMetricSpace(points);

stream = api.Plex4.createVietorisRipsStream(points, max_dimension + 1, max_filtration_value);
stream.finalizeStream();

densityFilterFunction = streams.filter.KernelDensityFilterFunction(metric_space, sigma);
densities = densityFilterFunction.getValues();
intFilterFunction = streams.filter.ExplicitIntFilterFunction(-densities);
simplexFilterFunction = streams.filter.InducedSimplicialFilterFunction(intFilterFunction);

multifilteredStream = streams.multi.BifilteredMetricStream(stream, simplexFilterFunction);

flattener = streams.multi.IncreasingOrthantFlattener(principal_direction);
flattened_stream = flattener.collapse(multifilteredStream);

persistenceAlgorithm = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension + 1);
barcodes = persistenceAlgorithm.computeIntervals(flattened_stream);

barcodes

%% Output

render_onscreen(flattened_stream, points);