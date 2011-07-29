clc; clear; close all;

T = 50000;

%{
k_min = 400;
k_max = 1000;
k_step = 1;

ensure_density_indices(T, k_min, k_max, k_step);
%}

sigma_min = 0.1;
sigma_max = 1;
sigma_step = 0.1;

ensure_kernel_density_indices(T, sigma_min, sigma_max, sigma_step);

sigma_min = 0.01;
sigma_max = 1;
sigma_step = 0.01;

ensure_kernel_density_indices(T, sigma_min, sigma_max, sigma_step);