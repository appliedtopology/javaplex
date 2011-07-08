clc; clear; close all;

load pointsRange.mat
size(pointsRange)

num_samples = 10;
sample_size = 40;
max_dimension = 1;

num_divisions = 500;

m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2

%% 
k_min = 100;
k_max = 300;
k_step = 100;
T = 300;

list = java.util.ArrayList();

for k = k_min:k_step:k_max
    densities = kDensitySlow(pointsRange, k);
    indices = int32(coreSubset(densities, T) - 1);
    indices = utility.ArrayUtility.makeMonotone(indices)
    list.add(indices);
end

%%
max_filtration_value = 0.08;
bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(pointsRange, max_filtration_value, max_dimension, list);

barcodes = bootstrapper.performBootstrap()

%% 

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

plot_barcodes(filtration_value_intervals);