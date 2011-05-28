% This shows the image patch example - Section 6

clc; clear; close all;

load pointsRange.mat
size(pointsRange)

num_samples = 2;
sample_size = 40;
max_dimension = 1;

num_divisions = 500;

m_space = metric.impl.EuclideanMetricSpace(pointsRange)
r_max = metric.utility.MetricUtility.estimateDiameter(m_space) / 2
max_filtration_value = r_max / 5



%utility.RandomUtility.initializeWithSeed(0);
bootstrapper = homology.zigzag.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(pointsRange), max_filtration_value, max_dimension, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap()

%%

%%
pov_writer = pov.PovLandmarkSetWriter();

selection = bootstrapper.getLandmarkSelector(0);
pov_writer.writeToFile(selection, 'test0.pov');

selection = bootstrapper.getLandmarkSelector(1);
pov_writer.writeToFile(selection, 'test1.pov');
