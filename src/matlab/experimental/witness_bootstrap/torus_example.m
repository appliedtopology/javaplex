clc; clear; close all;

num_samples = 41;
sample_size = 30;

max_dimension = 2;
num_points = 10000;
max_filtration_value = 0.1;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getRandomSphereProductPoints(n, 1, 2);

metricSpace = metric.impl.EuclideanMetricSpace(points);

utility.RandomUtility.initializeWithSeed(0);
bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(points), maxDistance, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap([1, 2, 1])

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

plot_barcodes(filtration_value_intervals, 1, 1, 'torus_1', 'Landmark samples from random points on a torus', 'eps', false, 2);

