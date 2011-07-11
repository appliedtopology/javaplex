clc; clear; close all;

%%
utility.RandomUtility.initializeWithSeed(0);

sample_size = 50;

max_dimension = 1;
num_points = 300;
max_filtration_value = 0.1;

n = num_points;
maxDistance = max_filtration_value;

points = examples.PointCloudExamples.getRandomFigure8Points(n);
points = points * 0.8;
metricSpace = metric.impl.EuclideanMetricSpace(points);

%%

sample = (utility.RandomUtility.randomSubset(sample_size, num_points / 2));
sample_0 = sample.toArray()
sample = sample_0;

selector = metric.landmark.ExplicitLandmarkSelector(metricSpace, sample);

filename = 'figure-8-landmark-0.pov';
create_landmark_pov_file(selector, filename);
render_pov_file(filename);

%%

sample = (utility.RandomUtility.randomSubset(sample_size, num_points / 2));
sample_1 = sample.toArray() + (num_points / 2);
sample = sample_1;

selector = metric.landmark.ExplicitLandmarkSelector(metricSpace, sample);

filename = 'figure-8-landmark-1.pov';
create_landmark_pov_file(selector, filename);
render_pov_file(filename);

%%
sample_01 = [sample_0; sample_1]
sample = sample_01;

selector = metric.landmark.ExplicitLandmarkSelector(metricSpace, sample);

filename = 'figure-8-landmark-01.pov';
create_landmark_pov_file(selector, filename);
render_pov_file(filename);