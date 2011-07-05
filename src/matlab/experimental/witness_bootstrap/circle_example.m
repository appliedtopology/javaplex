clc; clear; close all;

num_samples = 1000;
sample_size = 8;

max_dimension = 1;
num_points = 1000;
max_filtration_value = 0.1;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getEquispacedCirclePoints(n);

metricSpace = metric.impl.EuclideanMetricSpace(points);

utility.RandomUtility.initializeWithSeed(0);
bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(points), maxDistance, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap()

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

plot_barcodes(filtration_value_intervals, 0, 1, 'Landmark samples from random points on circle', false, 2);

