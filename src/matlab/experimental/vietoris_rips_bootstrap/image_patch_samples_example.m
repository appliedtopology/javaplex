clc; clear; close all;

%% 
k_min = 13;
k_max = 17;
k_step = 1;
T = 15000;
S = 100;
max_filtration_value = 1.1;

image_patch_experiment(T, k_min, k_max, k_step, S, max_filtration_value);
