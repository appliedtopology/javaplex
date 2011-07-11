clc; clear; close all;

num_samples = 11;
sample_size = 40;

max_dimension = 1;
num_points = 1000;
max_filtration_value = 0.3;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getRandomSpherePoints(n, max_dimension);

metricSpace = metric.impl.EuclideanMetricSpace(points);

utility.RandomUtility.initializeWithSeed(0);
bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(points), maxDistance, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap()

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.caption = 'Landmark samples from random points on a circle'
options.filename = 'circle';
options.file_format = 'eps';
options.min_dimension = 0;
options.max_dimension = 1;

plot_barcodes(filtration_value_intervals, options);

