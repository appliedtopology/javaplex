clc; clear; close all;

num_samples = 101;
sample_size = 5;

max_dimension = 1;
num_points = 10000;
max_distance = 1.1;

points = examples.PointCloudExamples.getRandomSpherePoints(num_points, max_dimension);
%points = examples.PointCloudExamples.getRandomFigure8Points(num_points);


bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(points, max_distance, max_dimension, num_samples, sample_size);

barcodes = bootstrapper.performBootstrap()

%% 

label = sprintf('circle-long-%d', max_distance * 10);


transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.filename = sprintf('%s-barcodes', label);
options.file_format = 'eps';
options.caption = sprintf('Circle samples, maximum filtration = %f', max_distance);
options.line_width = 2;
plot_barcodes(filtration_value_intervals, options);
