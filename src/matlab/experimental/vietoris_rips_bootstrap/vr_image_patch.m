clc; clear; close all;

dimension = 1;

n = 20;

load pointsRange.mat
size(pointsRange)

% create the set of points
m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2

%%
k = 300;
T = 400;

densities = kDensitySlow(pointsRange, k);
indices = coreSubset(densities, T);

%%
num_divisions = 1000;
max_filtration_value = 0.08;


% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(pointsRange(indices, :), dimension + 1, max_filtration_value, num_divisions);

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

% compute intervals
intervals = persistence.computeIntervals(stream);


%% create the barcode plots
options.filename = 'primary_circle_example';
options.caption = sprintf('Image Patch Data with Density Filtration: k = %d, T = %d', k, T);
options.file_format = 'eps';
plot_barcodes(intervals, options)