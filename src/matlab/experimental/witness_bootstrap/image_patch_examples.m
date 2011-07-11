clc; clear; close all;

load pointsRange.mat
size(pointsRange)

num_samples = 51;
sample_size = 30;
max_dimension = 1;

num_divisions = 500;

m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2
max_filtration_value = r_max / 8

bootstrapper = homology.zigzag.bootstrap.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(pointsRange), max_filtration_value, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap([1, 1])

%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.caption = sprintf('Image patch dataset: %d samples of size %d', num_samples - 1, sample_size);
options.filename = sprintf('image-patch-samples-constrained-%d-%d', num_samples - 1, sample_size);
options.file_format = 'eps';
options.min_dimension = 0;
options.max_dimension = 1
options.line_width = 2;
plot_barcodes(filtration_value_intervals, options);

