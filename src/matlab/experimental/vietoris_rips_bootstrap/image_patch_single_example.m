clc; clear; close all;

%%

k_min = 25;
k_max = 500;
k_step = 25;

T_min = 25;
T_max = 300;
T_step = 25;

max_filtration_value = 0.04;

parfor k = k_min:k_step:k_max
    for T = T_min:T_step:T_max
        [barcodes, stream_size] = image_patch_vr(T, k, max_filtration_value, false)
    end
end