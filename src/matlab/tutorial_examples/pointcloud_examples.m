% This script demonstrates the use of various point clouds - Section 4.2
clc; clear; close all;

%% House Example

max_dimension = 2;
max_filtration_value = 4;
num_divisions = 100;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample()

length(point_cloud)

% various examples of metric space methods
m_space = metric.impl.EuclideanMetricSpace(point_cloud)
m_space.distance(m_space.getPoint(1), m_space.getPoint(3))
% NB: javaPlex uses 0-based arrays!
point = m_space.getPoint(0)

%% Figure 8 Example

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomFigure8Points(100);

plot(point_cloud(:,1), point_cloud(:,2), '.')
axis equal

%% Torus Example

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomTorusPoints(2000, 1, 2);

figure
plot3(point_cloud(:,1), point_cloud(:,2), point_cloud(:,3), '.')
axis equal

%% Sphere Product Example

% create the set of points
% The following gets 1000 points on the torus S^1 x S^1 in R^4
% One can use the same to get uniformly random points on S^k x ... x S^k
% (any finite product of k-spheres)
point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(100, 1, 2);

figure
plot3(point_cloud(:,1), point_cloud(:,3), point_cloud(:,4), '.')
axis equal
