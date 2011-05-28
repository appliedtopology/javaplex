clc; clear; close all;

n = 100;
maxDistance = 0.3;
num_samples = 2;
sample_size = 20;
max_dimension = 1;
points = examples.PointCloudExamples.getRandomFigure8Points(n);

metricSpace = metric.impl.EuclideanMetricSpace(points);

utility.RandomUtility.initializeWithSeed(0);
bootstrapper = homology.zigzag.WitnessBootstrapper(metric.impl.EuclideanMetricSpace(points), maxDistance, 1, num_samples, sample_size);
barcodes = bootstrapper.performProjectionBootstrap()


%%
pov_writer = pov.PovLandmarkSetWriter();

selection = bootstrapper.getLandmarkSelector(0);
pov_writer.writeToFile(selection, 'test0.pov');

selection = bootstrapper.getLandmarkSelector(1);
pov_writer.writeToFile(selection, 'test1.pov');

%%
pov_writer = pov.PovWriter();

selection = bootstrapper.getLandmarkSelector(0);
pov_writer.writeToFile(selection, 'test0.pov');

selection = bootstrapper.getLandmarkSelector(1);
pov_writer.writeToFile(selection, 'test1.pov');