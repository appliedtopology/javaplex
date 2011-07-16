clc; clear; close all;

%% 
k_min = 25;
k_max = 1000;
k_step = 25;
T = 300;
max_filtration_value = 0.014;

image_patch_experiment(T, k_min, k_max, k_step, max_filtration_value);
