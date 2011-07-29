clc; clear; close all;

num_samples = 7;
sample_size = 40;

max_dimension = 1;
num_points = 10000;
max_distance = 1.2;

points = examples.PointCloudExamples.getRandomFigure8Points(num_points);

bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(points, max_distance, max_dimension, num_samples, sample_size);

barcodes = bootstrapper.performBootstrap();

%% 
label = 'figure-8';


transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.filename = sprintf('%s-barcodes', label);
options.file_format = 'eps';
options.line_width = 2;
options.caption = sprintf('Figure-8 (samples = %d, sample size = %d)', num_samples, sample_size);
plot_barcodes(filtration_value_intervals, options);

%%

label = 'figure-8-vietoris-rips';

indices_0 = bootstrapper.getSubset(0)
metric_space_0 = metric.impl.EuclideanMetricSpace(utility.ArrayUtility.getSubset(points, indices_0));
stream_0 = streams.impl.VietorisRipsStream(metric_space_0, max_distance, max_dimension + 1);
stream_0.finalizeStream();
filename_0 = sprintf('%s-%d.pov', label, 0);
create_pov_file(stream_0, metric_space_0, filename_0);
render_pov_file(filename_0);


for j = 1:(num_samples - 1)
    
    indices_1 = bootstrapper.getSubset(j)
    metric_space_1 = metric.impl.EuclideanMetricSpace(utility.ArrayUtility.getSubset(points, indices_1));
    stream_1 = streams.impl.VietorisRipsStream(metric_space_1, max_distance, max_dimension + 1);
    stream_1.finalizeStream();
    filename_1 = sprintf('%s-%d.pov', label, j);
    create_pov_file(stream_1, metric_space_1, filename_1);
    render_pov_file(filename_1);
    %{
    indices_01 = utility.ArrayUtility.union(indices_0, indices_1);
    metric_space_01 = metric.impl.EuclideanMetricSpace(utility.ArrayUtility.getSubset(points, indices_01));
    stream_01 = streams.impl.VietorisRipsStream(metric_space_01, max_distance, max_dimension + 1);
    stream_01.finalizeStream();
    filename_01 = sprintf('%s-%d%d.pov', label, j-1, j);
    create_pov_file(stream_01, metric_space_01, filename_01);
    render_pov_file(filename_01);
    %}
    indices_0 = indices_1;
end

%%

barcodes
