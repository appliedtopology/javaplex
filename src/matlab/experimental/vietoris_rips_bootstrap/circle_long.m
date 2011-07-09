clc; clear; close all;

num_samples = 41;
sample_size = 40;

max_dimension = 1;
num_points = 10000;
max_distance = 1.2;

points = examples.PointCloudExamples.getRandomSpherePoints(num_points, max_dimension);

bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(points, max_distance, max_dimension, num_samples, sample_size);

barcodes = bootstrapper.performBootstrap()

%% 

label = sprintf('circle-long-%d', max_distance * 10);


transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.filename = sprintf('%s-barcodes', label);
options.file_format = 'eps';
options.caption = sprintf('Circle samples, maximum filtration = %f', max_distance);
plot_barcodes(filtration_value_intervals, options);
