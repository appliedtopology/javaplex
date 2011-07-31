clc; clear; close all;

dataset = 'n50000Dct';
filter_label = 'kernel';
T = 15000;
S = 50;
max_filtration_value = 1.1;
filter_function = @(points, sigma) gaussian_kernel_densities(points, points, sigma);
display_plot = false;
use_sequential_maxmin = true;

theta_values = [1.0:0.1:2.0];

for (theta = theta_values)
    [barcodes, stream_size] = filtered_barcodes(dataset, filter_label, T, theta, S, max_filtration_value, filter_function, display_plot, use_sequential_maxmin)
end
