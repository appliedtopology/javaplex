clc; clear; close all;

%utility.RandomUtility.initializeWithSeed(3);

n = 100;
maxDistance = 0.3;
num_samples = 3;
sample_size = 20;
max_dimension = 1;
points = examples.PointCloudExamples.getRandomFigure8Points(n);

metricSpace = metric.impl.EuclideanMetricSpace(points);

list = java.util.ArrayList;
		
list.add(metric.landmark.ExplicitLandmarkSelector(metricSpace, [1:5:n]));
list.add(metric.landmark.ExplicitLandmarkSelector(metricSpace, [1:5:n/2]));

bootstrapper = homology.zigzag.WitnessBootstrapper(metricSpace, list, max_dimension, maxDistance);


barcodes = bootstrapper.performProjectionBootstrap()


%%
selection = bootstrapper.getLandmarkSelector(0);
pov_writer = pov.PovLandmarkSetWriter();
pov_writer.writeToFile(selection, 'test0.pov');

selection = bootstrapper.getLandmarkSelector(1);
pov_writer = pov.PovLandmarkSetWriter();
pov_writer.writeToFile(selection, 'test1.pov');

%%
pov_writer = pov.PovWriter();

selection = bootstrapper.getLandmarkSelector(0);
stream = api.Plex4.createWitnessStream(selection, max_dimension + 1, maxDistance);
pov_writer.writeToFile(stream, selection.getUnderlyingMetricSpace(), 'stream0.pov');

selection = bootstrapper.getLandmarkSelector(1);
stream = api.Plex4.createWitnessStream(selection, max_dimension + 1, maxDistance);
pov_writer.writeToFile(stream, selection.getUnderlyingMetricSpace(), 'stream1.pov');