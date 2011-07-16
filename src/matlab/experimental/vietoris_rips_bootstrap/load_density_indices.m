clc; clear; close all;

k_min = 25;
k_max = 1000;
k_step = 25;
T = 15000;

label = 'n50000Dct';
datafile = sprintf('%s.mat', label);

load(datafile, label);
pointsRange = n50000Dct;
size(pointsRange);

max_dimension = 1;

i_min = 0;
i_max = (k_max - k_min) / k_step;

indices_cell_array = cell(i_max + 1, 0);

cache_file_prefix = sprintf('cached_density_ranks/%s', label);


parfor i = i_min:i_max
    k = k_min + i * k_step;
    indices = get_core_subset_cached(pointsRange, k, T, cache_file_prefix);
    indices_cell_array{i + 1} = indices;
end