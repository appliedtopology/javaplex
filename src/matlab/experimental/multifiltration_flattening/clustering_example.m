clc; clear; close all;

%% Load data

load Syn2d_5g1k3c
points = dataM_c1;

% mean center
points(:, 1) = points(:, 1) - mean(points(:, 1));
points(:, 2) = points(:, 2) - mean(points(:, 2));

points(:, 1) = points(:, 1) / max(abs(points(:, 1)));
points(:, 2) = points(:, 2) / max(abs(points(:, 2)));
%scatter(points(:, 1), points(:, 2));

%% Parameters

max_dimension = 0;
max_filtration_value = 0.2;
sigma = 0.1;
principal_direction = [1000, 5000];

metric_space = metric.impl.EuclideanMetricSpace(points);

stream = api.Plex4.createVietorisRipsStream(points, max_dimension + 1, max_filtration_value, 1000);
stream.finalizeStream();

densityFilterFunction = streams.filter.KernelDensityFilterFunction(metric_space, sigma);
densities = densityFilterFunction.getValues();
intFilterFunction = streams.filter.ExplicitIntFilterFunction(-densities);
simplexFilterFunction = streams.filter.MaxSimplicialFilterFunction(intFilterFunction);

multifilteredStream = streams.multi.BifilteredMetricStream(stream, simplexFilterFunction);

flattener = streams.multi.HalfplaneFlattener(principal_direction);
flattened_stream = flattener.collapse(multifilteredStream);

persistenceAlgorithm = api.PersistenceAlgorithmInterface.getBooleanSimplicialAbsoluteHomology(max_dimension + 1);
barcodes = persistenceAlgorithm.computeAnnotatedIntervals(flattened_stream);

%% Output
points(:, 3) = densities;
%render_onscreen(flattened_stream, points);

%%
options.filename = 'flattened_clusters.eps';
options.caption = 'Barcodes for flattened multifiltered complex';
options.file_format = 'eps';
plot_barcodes(barcodes, options);

%% Extract 5 longest intervals

dim_0_intervals = barcodes.getIntervalGeneratorPairsAtDimension(0);
longest_interval_pairs = edu.stanford.math.plex4.homology.barcodes.BarcodeUtility.getLongestAnnotatedBarcodes(dim_0_intervals, 5);

fprintf('Longest Intervals and their Generators:\n');
for i=0:4
    chain = longest_interval_pairs.get(i).getSecond();
    interval = longest_interval_pairs.get(i).getFirst();
    iterator = chain.iterator();
    fprintf('interval: %s, chain: %s\n', char(interval.toString()), char(chain.toString()));
    while (iterator.hasNext())
        element = iterator.next();
        filtration_value = multifilteredStream.getFiltrationValue(element);
        fprintf('element: %s, filtration value: %f, %f\n', char(element.toString()), filtration_value(1), -filtration_value(2));
    end
end