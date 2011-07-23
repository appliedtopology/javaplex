clc; clear; close all;

path = '../../../../data/natural_images';
label = 'n50000Dct';
datafile = sprintf('%s/%s.mat', path, label);
load(datafile, label);
points = n50000Dct;


cache_file_prefix = sprintf('%s/cached_density_ranks/%s', path, label);

k = 300;
T = 15000;

indices = get_core_subset_cached(points, k, T, cache_file_prefix);


path = '../../../../data/natural_images';
label = 'nk300c30Dct';
datafile = sprintf('%s/%s.mat', path, label);
load(datafile, label);

computed_core = points(indices, :);