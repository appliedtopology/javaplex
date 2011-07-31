clc; clear; close all;

sigma_step = 0.001;
sigma_max = 0.2;
sigma_min = sigma_step;

theta_values = [sigma_min:sigma_step:sigma_max];

dataset = 'n50000Dct';
filter_label = 'kernel';
filter_function = @(points, sigma) gaussian_kernel_densities(points, points, sigma);

ensure_density_indices(theta_values, dataset, filter_label, filter_function)