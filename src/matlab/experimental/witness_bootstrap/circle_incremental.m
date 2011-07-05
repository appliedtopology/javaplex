clc; clear; close all;

utility.RandomUtility.initializeWithSeed(5);

n = 4000;
max_sample_size = 82;
increment_size = 1;
initial_sample_size = 2;
max_dimension = 1;
maxDistance = 0.1;
set = [1:n] - 1;

sample_size = initial_sample_size;
sample = [];

points = examples.PointCloudExamples.getEquispacedCirclePoints(n);
metricSpace = metric.impl.EuclideanMetricSpace(points);

list = java.util.ArrayList;

num_samples = 0;

while (sample_size <= max_sample_size)
    sample = randsample(set, sample_size);
    list.add(metric.landmark.ExplicitLandmarkSelector(metricSpace, sample));
    num_samples = num_samples + 1;
    
    sample_size = sample_size + increment_size;
end

bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metricSpace, list, max_dimension, maxDistance);


barcodes = bootstrapper.performProjectionBootstrap()

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

plot_barcodes(filtration_value_intervals, 1, 1, 'incremental-circle-barcodes', 'Incremental samples from random points on a circle', 'eps', false, 2);



