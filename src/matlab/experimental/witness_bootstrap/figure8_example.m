clc; clear; close all;

utility.RandomUtility.initializeWithSeed(0);

num_samples = 41;
sample_size = 20;

max_dimension = 1;
num_points = 1000;
max_filtration_value = 0.1;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getRandomFigure8Points(n);
metricSpace = metric.impl.EuclideanMetricSpace(points);

bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(points), maxDistance, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap([1, 2])


%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.caption = 'Landmark samples from random points on a figure-8';
options.filename = 'figure-8-barcodes';
options.file_format = 'eps';
options.min_dimension = 1;
options.max_dimension = 1

plot_barcodes(filtration_value_intervals, options);

%%
selections = [];

indices = [6 7 10 15 16 17 21 22 23 24 25 26 27];

for i = 1:length(indices)
    index = indices(i);
    selections = [selections bootstrapper.getLandmarkSelector(index)];
end

list = java.util.ArrayList;

for i = 1:length(selections)
    list.add(selections(i));
end

new_bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metricSpace, list, max_dimension, maxDistance);
barcodes = new_bootstrapper.performProjectionBootstrap()

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.caption = 'Reselected landmark samples from random points on a figure-8';
options.filename = 'reselected-figure-8-barcodes';
options.file_format = 'eps';
options.min_dimension = 1;
options.max_dimension = 1

plot_barcodes(filtration_value_intervals, options);
%%

n = length(selections);
expected_betti_sequence = [1, 2]';
graph = zeros(n, n);

for i = 1:n
    for j = (i+1):n
        list = java.util.ArrayList;

        list.add(selections(i));
        list.add(selections(j));
        
        graph_bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metricSpace, list, max_dimension, maxDistance);
        barcodes = graph_bootstrapper.performProjectionBootstrap();
        
        betti_sequence = barcodes.getBettiSequence();
        
        if (expected_betti_sequence == betti_sequence)
            graph(i, j) = 1;
            graph(j, i) = 1;
        end
    end
end


%%
points = [cos(2 * pi * (1:n) / n)' sin(2 * pi * (1:n) / n)'];

g_handle = figure;
gplot(graph, points, 'g-o');
axis equal;
title('Connectivity graph for reselected points in figure-8');

for K = 1:n
    text(cos(2 * pi * (K) / n), sin(2 * pi * (K) / n), sprintf('%d', indices(K)))
end

set(gca,'XTickLabel',{''})
set(gca,'YTickLabel',{''})

saveas(g_handle, 'reselected-connectivity-figure-8', 'eps');