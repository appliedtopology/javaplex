clc; clear; close all;

num_samples = 20;
sample_size = 8;

max_dimension = 1;
num_points = 1000;
max_filtration_value = 0.1;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getRandomFigure8Points(n);
%points = examples.PointCloudExamples.getRandomSpherePoints(n, 2);

metricSpace = metric.impl.EuclideanMetricSpace(points);

expected_betti_sequence = [1, 2]';

samples = cell(num_samples, 1);

for i = 1:num_samples
    samples{i} = randsample(0:(num_points - 1), sample_size);
end

graph = zeros(num_samples, num_samples);

for i = 1:num_samples
    for j = (i+1):num_samples
        list = java.util.ArrayList;

        list.add(metric.landmark.ExplicitLandmarkSelector(metricSpace, samples{i}));
        list.add(metric.landmark.ExplicitLandmarkSelector(metricSpace, samples{j}));
        
        bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metricSpace, list, max_dimension, maxDistance);
        barcodes = bootstrapper.performProjectionBootstrap();
        
        betti_sequence = barcodes.getBettiSequence();
        
        if (expected_betti_sequence == betti_sequence)
            graph(i, j) = 1;
            graph(j, i) = 1;
        end
    end
end


%%
n = num_samples;

points = [cos(2 * pi * (1:n) / n)' sin(2 * pi * (1:n) / n)'];

g_handle = figure;
gplot(graph, points, 'g-o');
axis equal;
title('Connectivity graph for points in figure-8');

for K = 1:n
    text(cos(2 * pi * (K) / n), sin(2 * pi * (K) / n), sprintf('%d', K))
end

set(gca,'XTickLabel',{''})
set(gca,'YTickLabel',{''})

saveas(g_handle, 'connectivity-figure-8', 'pdf');