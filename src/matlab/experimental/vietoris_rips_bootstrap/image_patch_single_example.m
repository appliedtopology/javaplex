clc; clear; close all;

%%

k_min = 500;
k_max = 800;
k_step = 25;

T_min = 100;
T_max = 400;
T_step = 25;

max_filtration_value = 0.2;

i_min = 0;
i_max = (k_max - k_min) / k_step;

parfor i = i_min:i_max
    k = k_min + i * k_step;
    for T = T_min:T_step:T_max
        [barcodes, stream_size] = image_patch_vr(T, k, max_filtration_value, false)
    end
end