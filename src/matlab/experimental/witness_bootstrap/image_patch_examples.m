clc; clear; close all;

load pointsRange.mat
size(pointsRange)

num_samples = 10;
sample_size = 40;
max_dimension = 1;

num_divisions = 500;

m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2
max_filtration_value = r_max / 5

bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(pointsRange), max_filtration_value, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap()

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

plot_barcodes(filtration_value_intervals, 0, 1, 'Image patch samples', false, 2);

