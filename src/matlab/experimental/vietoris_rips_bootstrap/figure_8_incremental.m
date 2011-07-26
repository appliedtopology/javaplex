clc; clear; close all;

utility.RandomUtility.initializeWithSeed(5);

n = 10000;
max_sample_size = 152;
increment_size = 1;
initial_sample_size = 2;
max_dimension = 1;
max_filtration_value = 1.0;
set = [1:n] - 1;

sample_size = initial_sample_size;
sample = [];

points = examples.PointCloudExamples.getRandomFigure8Points(n);

metricSpace = metric.impl.EuclideanMetricSpace(points);

list = java.util.ArrayList;

num_samples = 0;

while (sample_size <= max_sample_size)
    sample = randsample(set, sample_size);
    list.add(int32(sample));
    num_samples = num_samples + 1;
    
    sample_size = sample_size + increment_size;
end

bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(points, max_filtration_value, max_dimension, list);
barcodes = bootstrapper.performBootstrap();


%%

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.caption = sprintf('Incremental samples from random points on a figure-8 (f_{max} = %2.3f)', max_filtration_value);
options.filename = 'incremental-figure-8-barcodes';
options.file_format = 'eps';
options.min_dimension = 0;
options.max_dimension = 1;
options.line_width = 2;

plot_barcodes(filtration_value_intervals, options);



