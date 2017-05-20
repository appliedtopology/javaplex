% This script demonstrates the use of the dense core subjects -
% Appendix A

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% Prime Numbers Example

% get a list of the first 500 primes
p = primes(3571)';
length(p)

% estimate the density at each point with k = 1
densities1 = kDensitySlow(p, 1);
% get the core subset of the top 10% densest points
core1 = coreSubset(p, densities1, 10)

% estimate the density at each point with k = 50
densities50 = kDensitySlow(p, 50);
% get the core subset of the top 10% densest points
core50 = coreSubset(p, densities50, 10)