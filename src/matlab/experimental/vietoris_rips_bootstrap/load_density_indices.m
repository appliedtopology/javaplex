clc; clear; close all;

T = 50000;

k_min = 25;
k_max = 1000;
k_step = 25;

ensure_density_indices(T, k_min, k_max, k_step);

k_min = 5;
k_max = 500;
k_step = 5;

ensure_density_indices(T, k_min, k_max, k_step);

k_min = 1;
k_max = 50;
k_step = 1;

ensure_density_indices(T, k_min, k_max, k_step);

k_min = 500;
k_max = 1000;
k_step = 5;

ensure_density_indices(T, k_min, k_max, k_step);

k_min = 50;
k_max = 100;
k_step = 1;

ensure_density_indices(T, k_min, k_max, k_step);

k_min = 100;
k_max = 400;
k_step = 1;

ensure_density_indices(T, k_min, k_max, k_step);

