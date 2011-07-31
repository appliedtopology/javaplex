clc; clear; close all;

dataset = 'n50000Dct';
filter_label = 'kernel';
T = 15000;
S = 60;
max_filtration_value = 1.1;
filter_function = @(points, sigma) gaussian_kernel_densities(points, points, sigma);
display_plot = true;
use_sequential_maxmin = true;

theta_values = [0.1:0.002:0.2];

[barcodes, indices] = filtered_bootstrap(dataset, filter_label, T, theta_values, S, max_filtration_value, filter_function, display_plot, use_sequential_maxmin);

