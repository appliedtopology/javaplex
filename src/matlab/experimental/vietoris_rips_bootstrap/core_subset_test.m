clc; clear; close all;

k = 1;
T = 20;
label = 'primes';

points = primes(3571)';

indices = get_core_subset_cached(points, k, T, label);
points(indices);

indices = get_core_subset_cached(points, k, T + 10, label);
points(indices);