clc; clear; close all;

%%

k_min = 15;
k_max = 15;
k_step = 25;

T_min = 15000;
T_max = 15000;
T_step = 25;

S = 400;

max_filtration_value = 1.3;

i_min = 0;
i_max = (k_max - k_min) / k_step;

parfor i = i_min:i_max
    k = k_min + i * k_step;
    for T = T_min:T_step:T_max
        [barcodes, stream_size] = image_patch_vr(T, k, S, max_filtration_value, true)
    end
end